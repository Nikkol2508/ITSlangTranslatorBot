$(function() {

//    const appendTask = function(data) {
//        var taskCode = '<h4>' + data.name + '</h4>' + 'Описание: ' + data.description;
//        $('#task-list').append('<div>' + taskCode + '</div>');
//    };

    //Adding task
    $('#save-task').click(function(){
        var data = $('#create-task form').serialize();
        $.post("/task/", data, function(response){alert('Создана задача с id: ' + response)});
        return false;
    });

    //Get description
    $(document).on('click', '.task-link', function() {
        var link = $(this);
        var taskId = link.data('id');
        $.ajax({
            method: "GET",
            url: '/task/' + taskId,
            success: function(response){
                var descCod = '<p> Описание: ' + response.description + '</p>';
                link.parent().append(descCod);

            }
        });
        return false;
    });

});

//function showHide(element_id) {
//                //Если элемент с id-шником element_id существует
//                if (document.getElementById(element_id)) {
//                    //Записываем ссылку на элемент в переменную obj
//                    var obj = document.getElementById(element_id);
//                    //Если css-свойство display не block, то:
//                    if (obj.style.display != "block") {
//                        obj.style.display = "block"; //Показываем элемент
//                    }
//                    else obj.style.display = "none"; //Скрываем элемент
//                }
//                //Если элемент с id-шником element_id не найден, то выводим сообщение
//                else alert("Элемент с id: " + element_id + " не найден!");
//            }



