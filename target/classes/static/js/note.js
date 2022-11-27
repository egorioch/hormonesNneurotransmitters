$(document).ready(function () {
    $('#note-form').submit(function (event) {
        event.preventDefault();
        make_note();
    });
});


function make_note() {
    let note = {};
    note['tag'] = $('#tag').val();
    note['text'] = $('#text').val();
    console.log("username: " + username);

    for (let key in note) {
        console.log("ключ: " + key + ", значение: " + note[key]);
    }

    console.log("полученный note(stringify): " + JSON.stringify(note));
    console.log("полученный note: " + note.tag + ", " + note.text);

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/sendNote",
        //превращаем объект в строку-json
        data: JSON.stringify(note),
        dataType: 'json',
        success: function (data) {
            console.log("note_tag: " + data['tag']);
            console.log("note_text: " + data['text']);

            $('#card_tag').html(data['tag']);
            $('#card_text').html(data['text']);
            $('#card_username').html(username);

            let my_new_card= "<img class=\"card-img-top\" src=\"/img/firewatch_table.jpg\" alt=\"Card image cap\">\n" +
            "                <div class=\"card-body\">\n" +
            "\n" +
            "                    <div class=\"m-2\">\n" +
            "                        <p class=\"card-text\" id=\"card_tag\">"+ data['tag'] +"</p>\n" +
            "                        <i class=\"card-text\" id=\"card_text\">"+ data['text'] +"</i>\n" +
            "                    </div>\n" +
            "                    <div class=\"card-footer text-muted container\">\n" +
            "                        <div class=\"row\">\n" +
            "                            <p class=\"col align-self-center\" id=\"card_username\">" + username +"</p>\n" +
            "                        </div>\n" +
            "                    </div>\n" +
            "                </div>"

            $('#my_new_card').html(my_new_card);

            $('#tag').val("");
            $('#text').val("")

            //добавление карточки в конец списка

        },
        error: function (e) {
            let formTagError = "Fill the tag!";
            let formTextError = "Please enter a message in the textarea.";
            console.log("e_responseText: " + e.responseText);
            const err = e.responseText.split(",");
            console.log("errors split: " + err);

            if (err[0] === formTagError || err[1] === formTagError) {
                let textError = "<div class=\"alert alert-danger\">" + err + "</div>";
                $('#textError').html(textError);
            }

            if (err[0] === formTextError || err[1] === formTextError) {
                let tagError = "<div class=\"alert alert-danger\">" + err + "</div>";
                $('#textError').html(tagError);
            }

            console.log("ERRRRROR: " + e.responseText);

        }
    })

}

