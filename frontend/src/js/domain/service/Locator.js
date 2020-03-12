export class Locator {
  constructor() {
  }

  registerServiceByClass(serviceClass, serviceInstance) {
    return this.registerServiceByName(serviceClass.name, serviceInstance);
  }

  registerServiceByName(serviceName, serviceInstance) {
    this[serviceName] = serviceInstance;
    return this;
  }

  getServiceByClass(serviceClass) {
    return this.getServiceByName(serviceClass.name);
  }

  getServiceByName(serviceName) {
    return this[serviceName] || null;
  }
}

export default Locator;