window.addEventListener("DOMContentLoaded", function() {
    ClassicEditor.create(document.getElementById("content"))
        .then((editor) => console.log(editor));
});