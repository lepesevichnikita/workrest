export class Serializable {
  toJson() {
    const jsonObject = Object.getOwnPropertyNames(this)
                             .reduce((result, propertyName) => {
                               result[propertyName] = this[propertyName];
                               return result;
                             }, {});
    return JSON.stringify(jsonObject);
  }

  fromJson(json) {
    const jsonObject = JSON.parse(json);
    Object.getOwnPropertyNames(jsonObject)
          .forEach((propertyName) => {
            if (Object.getOwnPropertyNames(this)
                      .includes(propertyName)) {
              this[propertyName] = jsonObject[propertyName];
            }
          });
  }
}

export default Serializable;