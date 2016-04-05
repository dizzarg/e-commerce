var formatter = new Intl.NumberFormat("ru", {
    style: "currency",
    currency: "RUB",
    minimumFractionDigits: 2
});

var Order = function (data, products) {
    var self = this;
    self.products = [];
    self.createOrder = function (data, products) {
        if (data.id)
            self.id = data.id;
        if (data.dateCreated) {
            var formatter = new Intl.DateTimeFormat("ru", {
                weekday: "long",
                year: "numeric",
                month: "long",
                day: "numeric",
                hour: "numeric",
                minute: "numeric",
                second: "numeric"
            });
            self.dateCreated = formatter.format(new Date(data.dateCreated));
        }
        if (data.dateShipped)
            self.dateShipped = new Date(data.dateShipped);
        if (data.productIds && data.productIds) {
            for (var i = 0; i < data.productIds.length; i++) {
                for (var j = 0; j < products.length; j++) {
                    if (products[j].id == data.productIds[i]) {
                        self.products.push(products[j]);
                        break;
                    }
                }
            }
        }
    };

    if (data && products) {
        self.createOrder(data, products);
    }
};

var Payment = function (data) {
    var self = this;
    self.orders = [];
    self.createPayment = function (data) {
        if (data.id)
            self.id = data.id;
        if (data.amount)
            self.amount = formatter.format(data.amount / 100);
    };

    if (data) {
        self.createPayment(data);
    }
};

var Product = function (data) {
    var self = this;
    self.createProduct = function (data) {
        if (data.id)
            self.id = data.id;
        if (data.title)
            self.title = data.title;
        if (data.category)
            self.category = data.category;
        if (data.manufacturer)
            self.manufacturer = data.manufacturer;
        if (data.description) {
            self.fullDescription = data.description;
            if (data.description.length > 120) {
                self.shortDescription = data.description.substring(0, 120) + '...';
            } else {
                self.shortDescription = data.description;
            }
        }
        if (data.img)
            self.img = data.img;
        if (data.price) {
            self.p = data.price;
            self.price = formatter.format(data.price / 100);
        }
    };
    if (data) {
        self.createProduct(data);
    }
};

var Customer = function (data) {
    var self = this;
    self.shoppingCart = ko.observableArray([]);
    self.products = ko.observableArray([]);
    self.totalPrice = ko.observable(0);

    self.createCustomer = function (data) {
        if (data.username)
            self.username = data.username;
        if (data.password)
            self.password = data.password;
        if (data.email)
            self.email = data.email;
        if (data.firstName)
            self.firstName = data.firstName;
        if (data.lastName)
            self.lastName = data.lastName;
        if (data.address)
            self.address = data.address;
        if (data.phoneNumber)
            self.phoneNumber = data.phoneNumber;
        if (data.shoppingCart && data.shoppingCart.productIds) {
            for (var i = 0; i < data.shoppingCart.productIds.length; i++) {
                self.shoppingCart.push(data.shoppingCart.productIds[i])
            }
        }
    };

    self.updateShoppingCart = function (products) {
        self.products.removeAll();
        var total = 0;
        for (var i = 0; i < self.shoppingCart().length; i++) {
            for (var j = 0; j < products.length; j++) {
                if (products[j].id == self.shoppingCart()[i]) {
                    self.products.push(products[j]);
                    total += products[j].p;
                    break;
                }
            }
        }
        self.totalPrice(formatter.format(total / 100));
    };

    if (data) {
        self.createCustomer(data);
    }
};
    