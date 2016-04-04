(function () {

    ApplicationModule();

    $('li').on('click', function () {
        $('.active').removeClass('active');
        $(this).addClass('active');
    });
})();