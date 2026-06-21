const state = { categories: [], products: [] };
const categoryModal = new bootstrap.Modal('#categoryModal');
const productModal = new bootstrap.Modal('#productModal');
const byId = (id) => document.getElementById(id);

async function request(url, options = {}) {
    const response = await fetch(url, options);
    if (response.ok) return response.status === 204 ? null : response.json();
    let message = 'Request failed.';
    try { const error = await response.json(); message = error.error || Object.values(error).join(' '); } catch (_) { /* use default */ }
    throw new Error(message);
}

function showAlert(message, type = 'success') {
    byId('alertContainer').innerHTML = `<div class="alert alert-${type} alert-dismissible fade show" role="alert">${escapeHtml(message)}<button type="button" class="btn-close" data-bs-dismiss="alert"></button></div>`;
}

function escapeHtml(value = '') {
    return String(value).replace(/[&<>'"]/g, character => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', "'": '&#39;', '"': '&quot;' })[character]);
}

function emptyRow(columns, message) { return `<tr><td colspan="${columns}" class="empty-state">${message}</td></tr>`; }
function price(value) { return new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(value); }

async function refreshReferenceData() {
    [state.categories, state.products] = await Promise.all([request('/api/categories'), request('/api/products')]);
    fillCategoryOptions();
    fillProductOptions();
}

function fillCategoryOptions() {
    const options = state.categories.map(category => `<option value="${category.id}">${escapeHtml(category.name)}</option>`).join('');
    byId('productCategory').innerHTML = `<option value="">No category</option>${options}`;
    byId('productCategoryFilter').innerHTML = `<option value="">All categories</option>${options}`;
}

function fillProductOptions() {
    const options = state.products.map(product => `<option value="${product.id}">${escapeHtml(product.name)} (${product.quantity} in stock)</option>`).join('');
    byId('movementProduct').innerHTML = options || '<option value="">No products available</option>';
    byId('movementFilter').innerHTML = `<option value="">All products</option>${state.products.map(product => `<option value="${product.id}">${escapeHtml(product.name)}</option>`).join('')}`;
}

async function renderDashboard() {
    await refreshReferenceData();
    byId('productCount').textContent = state.products.length;
    byId('categoryCount').textContent = state.categories.length;
    byId('outOfStockCount').textContent = state.products.filter(product => product.quantity === 0).length;
    const rows = state.products.length ? state.products.map(product => `<tr><td>${escapeHtml(product.name)}</td><td>${escapeHtml(product.category?.name || '—')}</td><td>${price(product.price)}</td><td class="${product.quantity === 0 ? 'stock-empty' : ''}">${product.quantity}</td></tr>`).join('') : emptyRow(4, 'No products yet.');
    byId('dashboardProducts').innerHTML = rows;
}

async function renderCategories() {
    await refreshReferenceData();
    byId('categoryRows').innerHTML = state.categories.length ? state.categories.map(category => `<tr><td class="fw-semibold">${escapeHtml(category.name)}</td><td>${escapeHtml(category.description || '—')}</td><td class="text-end"><button class="btn btn-sm btn-outline-primary" data-edit-category="${category.id}">Edit</button> <button class="btn btn-sm btn-outline-danger" data-delete-category="${category.id}">Delete</button></td></tr>`).join('') : emptyRow(3, 'No categories yet.');
}

async function renderProducts() {
    await refreshReferenceData();
    await filterProducts();
}

async function filterProducts() {
    const search = byId('productSearch').value.trim();
    const categoryId = byId('productCategoryFilter').value;
    const url = search ? `/api/products?search=${encodeURIComponent(search)}` : categoryId ? `/api/products?categoryId=${categoryId}` : '/api/products';
    const products = await request(url);
    byId('productRows').innerHTML = products.length ? products.map(product => `<tr><td class="fw-semibold">${escapeHtml(product.name)}</td><td>${escapeHtml(product.category?.name || '—')}</td><td>${price(product.price)}</td><td class="${product.quantity === 0 ? 'stock-empty' : ''}">${product.quantity}</td><td class="text-end"><button class="btn btn-sm btn-outline-primary" data-edit-product="${product.id}">Edit</button> <button class="btn btn-sm btn-outline-danger" data-delete-product="${product.id}">Delete</button></td></tr>`).join('') : emptyRow(5, 'No products found.');
}

async function renderMovements() {
    await refreshReferenceData();
    await filterMovements();
}

async function filterMovements() {
    const id = byId('movementFilter').value;
    const movements = await request(id ? `/api/movements/product/${id}` : '/api/movements');
    byId('movementRows').innerHTML = movements.length ? movements.map(movement => `<tr><td>${new Date(movement.createdAt).toLocaleString()}</td><td>${escapeHtml(movement.product?.name || '—')}</td><td><span class="badge text-bg-${movement.type === 'INCOME' ? 'success' : 'danger'}">${movement.type === 'INCOME' ? 'Income' : 'Outcome'}</span></td><td>${movement.amount}</td><td>${escapeHtml(movement.reason || '—')}</td></tr>`).join('') : emptyRow(5, 'No stock movements yet.');
}

function showView(name) {
    document.querySelectorAll('.view').forEach(view => view.classList.toggle('active', view.id === name));
    document.querySelectorAll('[data-view]').forEach(link => link.classList.toggle('active', link.dataset.view === name));
    ({ dashboard: renderDashboard, categories: renderCategories, products: renderProducts, movements: renderMovements })[name]().catch(error => showAlert(error.message, 'danger'));
}

function openCategory(id = null) {
    const category = state.categories.find(item => item.id === id);
    byId('categoryModalTitle').textContent = category ? 'Edit category' : 'Add category';
    byId('categoryId').value = category?.id || '';
    byId('categoryName').value = category?.name || '';
    byId('categoryDescription').value = category?.description || '';
    categoryModal.show();
}

function openProduct(id = null) {
    const product = state.products.find(item => item.id === id);
    byId('productModalTitle').textContent = product ? 'Edit product' : 'Add product';
    byId('productId').value = product?.id || '';
    byId('productQuantity').value = product?.quantity || 0;
    byId('productName').value = product?.name || '';
    byId('productDescription').value = product?.description || '';
    byId('productPrice').value = product?.price ?? '';
    byId('productCategory').value = product?.category?.id || '';
    productModal.show();
}

document.addEventListener('click', async event => {
    const link = event.target.closest('[data-view]');
    if (link) { event.preventDefault(); showView(link.dataset.view); }
    if (event.target.matches('[data-edit-category]')) openCategory(Number(event.target.dataset.editCategory));
    if (event.target.matches('[data-edit-product]')) openProduct(Number(event.target.dataset.editProduct));
    if (event.target.matches('[data-delete-category]') && confirm('Delete this category?')) { try { await request(`/api/categories/${event.target.dataset.deleteCategory}`, { method: 'DELETE' }); showAlert('Category deleted.'); renderCategories(); } catch (error) { showAlert(error.message, 'danger'); } }
    if (event.target.matches('[data-delete-product]') && confirm('Delete this product?')) { try { await request(`/api/products/${event.target.dataset.deleteProduct}`, { method: 'DELETE' }); showAlert('Product deleted.'); renderProducts(); } catch (error) { showAlert(error.message, 'danger'); } }
});

byId('newCategory').addEventListener('click', () => openCategory());
byId('newProduct').addEventListener('click', () => openProduct());
byId('refreshDashboard').addEventListener('click', () => renderDashboard().catch(error => showAlert(error.message, 'danger')));
byId('applyProductFilter').addEventListener('click', () => filterProducts().catch(error => showAlert(error.message, 'danger')));
byId('applyMovementFilter').addEventListener('click', () => filterMovements().catch(error => showAlert(error.message, 'danger')));

byId('categoryForm').addEventListener('submit', async event => {
    event.preventDefault();
    const id = byId('categoryId').value;
    const body = { name: byId('categoryName').value.trim(), description: byId('categoryDescription').value.trim() };
    try { await request(id ? `/api/categories/${id}` : '/api/categories', { method: id ? 'PUT' : 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(body) }); categoryModal.hide(); showAlert('Category saved.'); renderCategories(); } catch (error) { showAlert(error.message, 'danger'); }
});

byId('productForm').addEventListener('submit', async event => {
    event.preventDefault();
    const id = byId('productId').value;
    const categoryId = byId('productCategory').value;
    const body = { name: byId('productName').value.trim(), description: byId('productDescription').value.trim(), price: Number(byId('productPrice').value), quantity: Number(byId('productQuantity').value) };
    const suffix = categoryId ? `?categoryId=${categoryId}` : '';
    try { await request(id ? `/api/products/${id}${suffix}` : `/api/products${suffix}`, { method: id ? 'PUT' : 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(body) }); productModal.hide(); showAlert('Product saved.'); renderProducts(); } catch (error) { showAlert(error.message, 'danger'); }
});

byId('movementForm').addEventListener('submit', async event => {
    event.preventDefault();
    const productId = byId('movementProduct').value;
    if (!productId) return showAlert('Create a product before registering a movement.', 'warning');
    const body = { type: byId('movementType').value, amount: Number(byId('movementAmount').value), reason: byId('movementReason').value.trim() };
    try { await request(`/api/movements/product/${productId}`, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(body) }); byId('movementAmount').value = 1; byId('movementReason').value = ''; showAlert('Stock movement registered.'); renderMovements(); } catch (error) { showAlert(error.message, 'danger'); }
});

showView('dashboard');
