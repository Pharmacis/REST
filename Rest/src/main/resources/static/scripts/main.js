$('document').ready(function () {
    $.ajax('/user/userAuth', {
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
                    <td  id="userProfession${user.id}" > ${user.profession}</td>
                    <td  id="userRoles${user.id}"></td>
                    <td style="display:none;" id="userPassword${user.id}">${user.password}</td>
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
          //  $("#users").find(`${id}`).remove();
        }
    })
}
//добавляет роли пользователю

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
        let name = $(`#userName${id}`).text().trim().split(" ");
        let profession = $(`#userProfession${id}`).text().trim().split(" ");
        let roles = $(`#userRoles${id}`).text().trim().split(" ");
        $('#idEdite').val(id);
        $('#nameEdite').val(name);
        $('#professionEdite').val(profession);
        $('#passEdite').val("San")
        $.each(roles, function (key, value) {
            $(`#rolesEdit option:contains('${value}')`).prop('selected', true);
        });
    }
//кнопка в модалке редактирования
    $('.btn-primary').on('click', function (event) {
        event.preventDefault();
        let i = $('#idEdite').val();
        let arrayRole = $('#rolesEdit').val();
        let user = {
            id: $('#idEdite').val(),
            name: $('#nameEdite').val(),
            password: $('#passEdite').val(),
            profession: $('#professionEdite').val()
        }
        addRole(arrayRole, user);
        $.ajax('/users/' + i, {
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
        let arrayRole = $('#addRole').val();
        let user = {
            name: $('#addName').val(),
            profession: $('#addProfession').val(),
            password: $('#addPass').val()
        }
        addRole(arrayRole, user);
        $.ajax('/users', {
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
function addRole(arrayRole,user){
    if (arrayRole.length > 1) {
        user.roles = [
            {
                id: arrayRole[0].slice(0, 1),
                name:
                    arrayRole[0].slice(2)
            },
            {
                id: arrayRole[1].slice(0, 1),
                name:
                    arrayRole[1].slice(2)
            }
        ]
    } else {
        user.roles = [
            {
                id: arrayRole[0].slice(0,1),
                name:
                    arrayRole[0].slice(2)
            }
        ]
    }
}

