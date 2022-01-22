
function post() {
    // 获取元素
    var question_id = $("#question_id").val();
    var content = $("#content").val();

    if (!content) {
        alert("内容不能为空");
        return;
    }

    $.ajax({
        type: "post",
        contentType: "application/json",
        url: "/comment",
        data: JSON.stringify({
            "parent_id": question_id,
            "content": content,
            "type": 1
        }),
        success: function (response) {
            if (response.code === 2000) {
                // 刷新
                window.location.reload();
            } else {
                if (response.code === 4001) {
                    var flag = confirm(response.msg); // boolean
                    if (flag) {
                        window.open("https://github.com/login/oauth/authorize?client_id=e8ddec605b8d0da8a386&redirect_uri=http://localhost:8001/callback&scope=user&state=1");
                        window.localStorage.setItem("closable", "true");
                    }
                } else {
                    alert(response.msg);
                }
            }
        },
        dataType: "json"
    });

}