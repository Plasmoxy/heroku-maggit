
$(function() {
  console.log("=== main.js running ===")

  let updatews = new WebSocket(window.location.host + ":7000/websocket")

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
