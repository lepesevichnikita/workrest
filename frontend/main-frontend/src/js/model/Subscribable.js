export class Subscribable {
  constructor() {
    this._subscribers = {};
  }

  subscribe(actionName, callback) {
    const callbacks = this._subscribers[actionName] || [];
    this._subscribers[actionName] = [...callbacks, callback];
    return this;
  }

  notifyAllSubscribers(actionName, data) {
    const callbacks = this._subscribers[actionName] || [];
    callbacks.forEach(callback => callback(data));
  }
}

export default Subscribable;