/**
 *  评论
 */
function comment() {
    var question_id = $("#question_id").val();
    var content = $("#content").val();

    post(question_id, 1, content);
}

/**
 *  回复
 */
function reply(e) {
    var commentId = e.getAttribute("data-id");
    var content = $("#reply-" + commentId).val();
    post(commentId, 0, content);
}

/**
 * 提交回复，根据参数确定评论还是回复
 */
function post(targetId, type, content) {
    if (!content) {
        alert("内容不能为空");
        return;
    }

    $.ajax({
        type: "post",
        contentType: "application/json",
        url: "/comment",
        data: JSON.stringify({
            "parent_id": targetId,
            "content": content,
            "type": type
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

/**
 * 展开或折叠子评论
 * @param e 评论 字体图标元素
 */
function subComments(e) {
    var id = e.getAttribute("data-id");
    var sub = $("#comment-" + id);

    // 获取当前元素的属性
    var collapse = e.getAttribute("data-collapse");

    if (collapse) {
        // 折叠
        sub.removeClass("in");
        e.removeAttribute("data-collapse");
        e.classList.remove("active");
    } else {
        // 展开
        var subCommentContainer = $("#comment-" + id);

        if (subCommentContainer.children().length !== 1) {
            sub.addClass("in");
            e.setAttribute("data-collapse", "in");
            e.classList.add("active");
        } else {
            $.getJSON("/comment/" + id, function (data) {
                $.each(data.data.reverse(), function (index, comment) {

                    var mediaLeftElement = $("<div/>", {
                        "class": "media-left"
                    }).append($("<img/>", {
                        "class": "media-object img-rounded",
                        "src": comment.user.avatar_url
                    }));

                    var mediaBodyElement = $("<div/>", {
                        "class": "media-body"
                    }).append($("<h5/>", {
                        "class": "media-heading",
                        "html": comment.user.name
                    })).append($("<div/>", {
                        "html": comment.content
                    })).append($("<div/>", {
                        "class": "menu"
                    }).append($("<span/>", {
                        "class": "pull-right",
                        "html": moment(comment.gmt_create).format('YYYY-MM-DD')
                    })));

                    var mediaElement = $("<div/>", {
                        "class": "media"
                    }).append(mediaLeftElement).append(mediaBodyElement);

                    var commentElemnt = $("<div/>", {
                        "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments"
                    }).append(mediaElement);

                    subCommentContainer.prepend(commentElemnt);
                });
                //展开二级评论
                sub.addClass("in");
                // 标记二级评论展开状态
                e.setAttribute("data-collapse", "in");
                e.classList.add("active");
            });
        }
    }
}