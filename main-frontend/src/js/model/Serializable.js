export class Serializable {
  toJson() {
    const jsonObject = Object.getOwnPropertyNames(this)
                             .reduce((result, propertyName) => {
                               result[propertyName] = this[propertyName];
                               return result;
                             }, {});
    return JSON.stringify(jsonObject);
  }
}

export default Serializable;