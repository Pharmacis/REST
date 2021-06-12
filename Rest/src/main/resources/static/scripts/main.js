$('document').ready(function () {
    $.ajax('/users/userAuth', {
        method: 'GET',
        success: function (user) {
            $('#nameTitle').text(user.name);
            user.roles.forEach(function (role) {
                $('#roleTitle').append(`<strong> ${role.name} </strong>`);
            })
        }
    })
    showUsers();
})

//строит таблицу всех юзеров
function showUsers() {
    $('#users').empty();
    $.ajax("/users", {
        dataType: "json",
        method: 'GET',
        success: function (data) {
            let users = JSON.parse(JSON.stringify(data));
            users.forEach(function (user) {
                $("#users").append(`<tr id="tr${user.id}"> 
                    <td  id="userId${user.id}" > ${user.id}</td> 
                    <td  id="userName${user.id}" > ${user.name}</td>
                    <td  id="userName${user.id}" > ${user.profession}</td>
                    <td  id="userRoles${user.id}"></td>
                    <td>
                    <button class="btn btn-info" type="button" data-toggle="modal" data-target="#edit" onclick="openEditeModal( ${user.id})">Edit</button>
                    </td>
                    <td>
                    <button class="btn btn-danger" type="button" data-toggle="modal" data-target="#delete" onclick="openDeleteModal(${user.id})">Delete</button>
                    </td>
                    </tr>`);
                user.roles.forEach(function (role) {
                    $(`#userRoles${user.id}`).append('<span>' + role.simpleName + ' </span>');
                })
            })
            $("#users").find(`#tr${id}`).remove();
        }
    })
}

//открывает модалку для удаления
function openDeleteModal(id) {
    let name = $(`#userName${id}`).text()
    let roles = $(`#userRoles${id}`).text().trim().split(" ");
    $('#delete #id').val(id);
    $('#delete #name').val(name);
    $('#delete #roles').empty();
    $.each(roles, function (key, value) {
        $('#delete #roles').append(`<option value='key'>' ${value} </option>`);
    });
}

//кнопка в модалке удаляет юзверя
$('#deleteUser').on('click', function deleteUser() {
    let id = $('#delete #id').val();
    $.ajax('/users/' + id, {
        method: 'DELETE',
        success: function () {
            $("#users").find(`#tr${id}`).remove();
        }
    })
})

//открывает модалку редактирования
function openEditeModal(id) {
    let name = $(`#userName${id}`).text()
    $('#idEdite').val(id);
    $('#nameEdite').val(name);
    $('#professionEdite').val();
}

//кнопка в модалке редактирования
$('.btn-primary').on('click', function (event) {
    event.preventDefault();
    let user = {
        id: $('#idEdite').val(),
        name: $('#nameEdite').val(),
        password: $('#passEdite').val(),
        profession:  $('#professionEdite').val(),
        roles :[
            {
                name: $('#rolesEdite').val().join()
            }]

    };


    $.ajax('/users/edit', {
        data: JSON.stringify(user),
        dataType: 'json',
        contentType: 'application/JSON; charset=utf-8',
        method: 'PUT',
        success: function () {
            showUsers();
        }
    })
})

//добавление юзера
$('.btn-success').on('click', function (event) {
    event.preventDefault();
    let user;
    user = {
        name: $('#addName').val(),
        profession:  $('#addProfession').val(),
        password: $('#addPass').val(),
        roles: [{name: $('#addRole').val().join()}]
    };
    $.ajax('/users/add', {
        data: JSON.stringify(user),
        dataType: 'json',
        contentType: 'application/JSON; charset=utf-8',
        method: 'POST',
        success: function () {
            $('#tab1').addClass('active');
            $('#tab-1').addClass('active');
            $('#tab2').removeClass('active');
            $('#tab-2').removeClass('active');
            $('#addName').val('User name');
            $('#addPass').val('Password');
            showUsers();
        }
    })
})
