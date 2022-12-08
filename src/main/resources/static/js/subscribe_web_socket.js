ws=new WebSocket("ws://localhost:8080/sub_action")

ws.onopen = function () {
    console.log("connection established");
}
ws.onmessage = function (ev) {
    let event_data = ev.data;
    console.log("ev.data = " + event_data);
    if (event_data === "Subscribe") {
        console.log("СоВПалО! " + event_data);
        document.getElementById('to_sub').value = "Unsubscribe";
    } else if (event_data === "Unsubscribe"){
        console.log("СоВПалО! " + event_data);
        document.getElementById('to_sub').value = "Subscribe";
    }

}

ws.onerror=function(ev) {}
ws.onclose=function(ev) {}

function server_answer(btn_val) {
    console.log("server_answer: " + btn_val.value)
    if (btn_val.value === "Subscribe") {
        document.getElementById('to_sub').value = "Unsubscribe";
    } else {
        document.getElementById("to_sub").value = "Subscribe";
    }
}

function sub_action(btn) {
    let message = btn.value + ',' + isSubscriber + ',' + userChannelUsername + ',' + userUsername;
    //server_answer(btn);
    ws.send(message);
}
