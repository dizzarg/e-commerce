
var ApplicationVM = function (serverModule) {
    var self = this;
    self.server = serverModule;
    self.searchText = ko.observable("");
    self.allProducts = ko.observableArray([]);
    self.products = ko.computed(function() {
        var search = self.searchText().trim().toLowerCase();
        if(search.length==0) return self.allProducts();
        return $.grep(self.allProducts(), function(product) {
            return product.title.toLowerCase().indexOf(search) >= 0;
        });
    });
    self.orders = ko.observableArray([]);
    self.currentOrder = ko.observable();
    self.payments = ko.observableArray([]);
    self.customer = ko.observable();
    self.selectedProduct = ko.observable();
    self.redirectToListPage = function () {
        window.location.hash = "/products";
    };
    self.addProduct = function (product) {
        self.server.addProductToCustomer(product.id, self.customer().username, function () {
            self.customer().shoppingCart.push(product.id);
            self.customer().updateShoppingCart(self.allProducts());
        });
    };
    self.removeProduct = function (product) {
        self.server.removeProductFromCustomer(product.id, self.customer().username, function () {
            self.customer().shoppingCart.remove(product.id);
            self.customer().updateShoppingCart(self.allProducts());
            if(self.customer().shoppingCart().length<=0)
                window.location.hash = "/";
        });
    };
    self.createPayment = function (order) {
        self.server.createPayment(order, 1,  function (data) {
            window.location.hash = '/';
        });
    };
    self.server.loadCustomer('Vernon', function (data) {
        self.customer(new Customer(data));
    });
    self.server.loadProducts(function (data) {
        for (var i=0; i<data.length; i++){
            var product = new Product(data[i]);
            self.allProducts.push(product);
        }
    });

};

var ServerModule = function() {
    var self = this;
    self.loadProducts = function (callback) {
        $.getJSON('/api/products', function (data) {
            if(callback){
                callback(data)
            }
        });
    };
    self.loadCustomer = function (userName, callback) {
        $.getJSON('/api/customers/'+userName, function (data) {
            if(callback){
                callback(data)
            }
        });
    };
    self.loadOrders = function (userName, callback) {
        $.getJSON('/api/customers/'+userName+'/orders', function (data) {
            if(callback){
                callback(data)
            }
        });
    };
    self.loadPayments = function (callback) {
        $.getJSON('/api/payments', function (data) {
            if(callback){
                callback(data)
            }
        });
    };
    self.addProductToCustomer = function (id, userName, callback) {
        $.post('/api/shop/add',{productId: id, userName: userName, amount: 1}, function () {
            if(callback){
                callback()
            }
        });
    };
    self.removeProductFromCustomer = function (id, userName, callback) {
        $.post('/api/shop/remove',{productId: id, userName: userName, amount: 1}, function () {
            if(callback){
                callback()
            }
        });
    };
    self.createOrder = function (userName, callback) {
        $.post('/api/orders',{userName: userName},function (data) {
            if(callback){
                callback(data)
            }
        });
    };

    self.createPayment = function (order, amount, callback) {
        $.post('/api/payments', {orderId: order.id, amount:amount}, function (data) {
            if(callback){
                callback(data)
            }
        });
    };
};

var ApplicationModule = function () {
    var self = this;
    self.server = new ServerModule();
    self.appvm = new ApplicationVM(self.server);
    var routes = {
        '/': function () {
            self.appvm.searchText('');
            $('#content').load('/products/list', function () {
                ko.applyBindings(self.appvm, document.getElementById("products"));
            });
        },
        '/products': function () {
            $('#content').load('/products/list', function () {
                ko.applyBindings(self.appvm, document.getElementById("products"));
            });
        },
        '/product/:id': function (id) {
            self.appvm.searchText('');
            $('#content').load('/products/product', function () {
                var prod = ko.utils.arrayFirst(self.appvm.products(), function (item) {
                    return item.id == id;
                });
                self.appvm.selectedProduct(prod);
                ko.applyBindings(self.appvm, document.getElementById("product"));
            });
        },
        '/cart': function () {
            self.appvm.searchText('');
            $('#content').load('/products/shoppingCart', function () {
                self.appvm.customer().updateShoppingCart(self.appvm.products());
                ko.applyBindings(self.appvm, document.getElementById("cart"));
            });
        },
        '/orders': function () {
            self.appvm.searchText('');
            $('#content').load('/orders/load', function () {
                self.server.loadOrders(self.appvm.customer().username, function (data) {
                    self.appvm.orders.removeAll();
                    for (var i =0 ;i<data.length; i++) {
                        self.appvm.orders.push(new Order(data[i], self.appvm.products()))
                    }
                    ko.applyBindings(self.appvm, document.getElementById("orders"));
                });
            });
        },
        '/orders/create': function () {
            self.appvm.searchText('');
            self.server.createOrder(self.appvm.customer().username, function (data) {
                self.appvm.customer().shoppingCart.removeAll();
                $('#content').load('/orders/order', function () {
                    self.appvm.currentOrder(new Order(data, self.appvm.products()));
                    ko.applyBindings(self.appvm, document.getElementById("order"));
                });
            });
        },
        '/payments': function () {
            self.appvm.searchText('');
            $('#content').load('/payments/load', function () {
                self.server.loadPayments(function (data) {
                    self.appvm.payments.removeAll();
                    for (var i =0 ;i<data.length; i++) {
                        self.appvm.payments.push(new Payment(data[i]))
                    }
                    ko.applyBindings(self.appvm, document.getElementById("payments"));
                });
            });
        }
    };
    ko.applyBindings(self.appvm);
    var router = Router(routes);
    router.init('#/');
};

new ApplicationModule();

$('li').on('click', function () {
    $('.active').removeClass('active');
    $(this).addClass('active');
});