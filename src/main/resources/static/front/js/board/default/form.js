window.addEventListener("DOMContentLoaded", function() {
    const { loadEditor } = commonLib;

    loadEditor("content", 350)
        .then(editor => window.editor = editor);
});