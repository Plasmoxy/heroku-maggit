
function getWSWithProtocol() {
  if (window.location.protocol == "https:") {
    return "wss://"
  } else {
    return "ws://"
  }
}

$(function() {
  console.log("=== main.js running ===")

  let updatews = new WebSocket(getWSWithProtocol() + window.location.host + "/timews")
  let updatetimer

  updatews.onopen = () => {
    console.log("connected to websocket")
    updatetimer = setInterval(() => {
      updatews.send("getTime")
    }, 1000)
  }

  updatews.onmessage = (e) => {
    console.log(`received message : ${e.data}`)
    $('#timeText').text(e.data)
  }

  updatews.onclose = () => {
    console.log("websocket closed")
    if (updatetimer) clearInterval(updatetimer)
  }

  updatews.onerror = (err) => {

  }
})
