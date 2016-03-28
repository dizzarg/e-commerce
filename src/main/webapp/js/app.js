(function () {

    var Server = function() {
        var self = this;
        self.loadProducts = function (callback) {
            $('#content').load('/products/list', function () {
               if(callback)
                   callback();
            });
        };
    };

    var server = new Server();

    var index = function () {
        server.loadProducts(function () {
                applicationViewModel.loadAll();
                // Загружать посте авторизации или верификации пользователя
                applicationViewModel.loadCustomer(function () {
                    ko.cleanNode(document.getElementById('main-navbar'));
                    ko.applyBindings(applicationViewModel);
                });
        });
    };
    var products = function () {
        $('#content').load('/products/list', function () {
            applicationViewModel.loadAll();
            ko.applyBindings(applicationViewModel, document.getElementById("products"));
        });
    };
    var product = function (productId) {
        $('#content').load('/products/product', function () {
            applicationViewModel.getProduct(productId);
            ko.applyBindings(applicationViewModel, document.getElementById("product"));
        });
    };
    var shoppingCart = function () {
        $('#content').load('/products/shoppingCart', function () {
            if(applicationViewModel.products().length == 0){
                applicationViewModel.loadAll();
            }
            applicationViewModel.loadCustomer(function () {
                ko.applyBindings(applicationViewModel, document.getElementById("cart"));
            });
        });
    };
    var orders = function () {
        $('#content').load('/orders/load', function () {
            applicationViewModel.loadOrders();
            ko.applyBindings(applicationViewModel, document.getElementById("orders"));
        });
    };
    var addProduct = function () {
        $('#content').load('/products/add', function () {

        });
    };
    var logout = function () {

    };
    var profile = function () {

    };
    var routes = {
        '/': [index],
        '/products': [products],
        '/product/:id': [product],
        '/cart': [shoppingCart],
        '/logout': [logout],
        '/orders': [orders],
        '/profile': [profile]
    };

    var router = Router(routes);

    var ShoppingCartModel = function () {
        var self = this;
        self.items = ko.observableArray([]);
        self.add = function (item, callback) {
            $.post('/api/shop/add',{productId: item.product.id, userName: 'Vernon', amount: 1}, function (data) {
                self.items.push(item);
                if(callback)
                    callback();
            });
        };
        self.remove = function (item, callback) {
            $.post('/api/shop/remove',{productId: item.product.id, userName: 'Vernon', amount: 1}, function (data) {
                self.items.remove(function (el) { return item.product.id == el.product.id; });
                if(callback)
                    callback();
            });
        };
        self.clean = function () {
            self.items.removeAll();
            window.location.href = "/";
        }
    };

    var ApplicationViewModel = function() {
        var self = this;
        self.products = ko.observableArray([]);
        self.orders = ko.observableArray([]);
        self.cart = ko.observable(new ShoppingCartModel());
        self.product = ko.observable();
        self.addProduct = function () {
            self.cart().add({product: self.product(), count: 1}, function () {
                ko.cleanNode(document.getElementById('main-navbar'));
                ko.applyBindings(applicationViewModel, document.getElementById('main-navbar'));
            });
        };
        self.removeProduct = function () {
            self.cart().remove({product: self.product(), count: 1}, function () {
                ko.cleanNode(document.getElementById('main-navbar'));
                ko.applyBindings(applicationViewModel);
            });
        };
        self.getProduct = function (id) {
            var prod = ko.utils.arrayFirst(self.products(), function (item) {
                return item.id == id;
            });
            self.product = ko.observable(prod);
        };
        self.makeOrder = function () {
            $.post('/api/orders',{userName: 'Vernon'},function () {
                self.cart().clean();
            });
        };
        self.loadCustomer = function(callback){
            $.getJSON('/api/customers/Vernon', function (data) {
                var prods = data.shoppingCart.productIds;
                var shoppingCartModel = new ShoppingCartModel();
                for (var j=0; j<prods.length; j++){
                    for(var i=0; i<self.products().length; i++) {
                        if (self.products()[i].id == prods[j]) {
                            shoppingCartModel.items().push({product: self.products()[i], count: 1});
                            break;
                        }
                    }
                }
                self.cart = ko.observable(shoppingCartModel);
                if(callback){
                    callback();
                }
            });
        };
        self.loadAll = function(){
            $.getJSON('/api/products', function (data) {
                self.products(data);
            });
        };
        self.loadOrders = function(){
            $.getJSON('/api/customers/Vernon/orders', function (data) {
                self.orders(data);
            });
        };
    };
    var applicationViewModel = new ApplicationViewModel();
    ko.applyBindings(applicationViewModel, document.getElementById('main-navbar'));

    router.init('#/');
    $('li').on('click', function () {
        $('.active').removeClass('active');
        $(this).addClass('active');
    });
})();