// socketHandler.js
let io; // Define the 'io' variable outside the function

function setIO(ioInstance) {
  io = ioInstance; // Assign the 'io' instance to the variable
}

function updateWebClients(event, details) {
  io.emit(event, details); // Emit the event to all connected clients
}

module.exports = {
  setIO,
  updateWebClients
};
