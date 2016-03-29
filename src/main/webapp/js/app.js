(function () {

    var Customer = function (data) {
        var self = this;
        self.shoppingCart = ko.observableArray([]);
        
        self.createCustomer = function (data) {
            if(data.username)
                self.username = data.username;
            if(data.password)
                self.password = data.password;
            if(data.email)
                self.email = data.email;
            if(data.firstName)
                self.firstName = data.firstName;
            if(data.lastName)
                self.lastName = data.lastName;
            if(data.address)
                self.address = data.address;
            if(data.phoneNumber)
                self.phoneNumber = data.phoneNumber;
            if(data.shoppingCart && data.shoppingCart.productIds){
                for (var i=0; i<data.shoppingCart.productIds.length; i++){
                    self.shoppingCart.push(data.shoppingCart.productIds[i])
                }
            }
        };
        if(data){
            self.createCustomer(data);
        }
    };

    var ApplicationVM = function (serverModule) {
        var self = this;
        self.server = serverModule;
        self.products = ko.observableArray([]);
        self.orders = ko.observableArray([]);
        self.customer = ko.observable();
        self.selectedProduct = ko.observable();
        self.addProduct = function () {
            self.server.addProductToCustomer(self.selectedProduct().id, self.customer().username, function () {
                self.customer().shoppingCart.push(self.selectedProduct().id);
            });
        };
        self.removeProduct = function (id) {
            self.server.removeProductFromCustomer(id, self.customer().username, function () {
                self.customer().shoppingCart.remove(id);
                if(self.customer().shoppingCart().length<=0)
                    window.location.href = "/";
            });
        };
        self.makeOrder = function () {
            self.server.makeOrder(self.customer().username, function () {
                self.customer().shoppingCart.removeAll();
                window.location.href = "/";
            });
        };
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
        self.makeOrder = function (userName, callback) {
            $.post('/api/orders',{userName: userName},function () {
                if(callback){
                    callback()
                }
            });
        }
    };

    var ApplicationModule = function () {
        var self = this;
        self.server = new ServerModule();
        self.appvm = new ApplicationVM(self.server);
        var routes = {
            '/': function () {
                $('#content').load('/products/list', function () {
                    self.server.loadProducts(function (data) {
                        self.appvm.products(data);
                    });
                    self.server.loadCustomer('Vernon', function (data) {
                        self.appvm.customer(new Customer(data));
                    });
                    ko.applyBindings(self.appvm, document.getElementById("products"));
                });
            },
            '/products': function () {
                $('#content').load('/products/list', function () {
                    self.server.loadProducts(function (data) {
                        self.appvm.products(data);
                    });
                    self.server.loadCustomer('Vernon', function (data) {
                        self.appvm.customer(new Customer(data));
                    });
                    ko.applyBindings(self.appvm, document.getElementById("products"));
                });
            },
            '/product/:id': function (id) {
                $('#content').load('/products/product', function () {
                    var prod = ko.utils.arrayFirst(self.appvm.products(), function (item) {
                        return item.id == id;
                    });
                    self.appvm.selectedProduct(prod);
                    ko.applyBindings(self.appvm, document.getElementById("product"));
                });
            },
            '/cart': function () {
                $('#content').load('/products/shoppingCart', function () {
                    ko.applyBindings(self.appvm, document.getElementById("cart"));
                });
            },
            '/orders': function () {
                $('#content').load('/orders/load', function () {
                    self.server.loadOrders(self.appvm.customer().username, function (data) {
                        self.appvm.orders(data);
                        ko.applyBindings(self.appvm, document.getElementById("orders"));
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
})();