$('document').ready(function () {
    $.ajax('user/userAuth', {
        success: function (user) {
            $('#nameTitle').text(user.name);
            $('#id').text(user.id);
            $('#name').text(user.name);
            $('#profession').text(user.profession);
            user.roles.forEach(function (role) {
                $('#roleTitle').append('<strong>' + role.name + ' </strong>');
                $('#role').append('<span>' + role.name + ' </span>');
                if (role.name === 'ROLE_ADMIN') {
                    $('#nav').prepend('<li class="nav-item"><a class="nav-link border rounded" href="/admin">Admin</a></li>');
                }
            })
        }
    })
})