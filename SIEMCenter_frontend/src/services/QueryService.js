export const QueryService = {
  formQuery,
};

function formQuery(queryObject) {
  let queryObjectCopy = removeEmpty(queryObject);
  let params = new URLSearchParams();
  Object.keys(queryObjectCopy).forEach(
    (key) => {
      if (Array.isArray(queryObjectCopy[key])) {
        queryObjectCopy[key].forEach(x => params.append(key, x))
      } else {
        params.append(key, queryObjectCopy[key])
      }
    }
  )
  return params;
}

const removeEmpty = (obj) => {
  const objCopy = JSON.parse(JSON.stringify(obj));
  Object.keys(objCopy).forEach(
    (key) =>
      (objCopy[key] === null ||
        objCopy[key] === undefined ||
        (Array.isArray(objCopy[key]) && objCopy[key].length === 0)) &&
      delete objCopy[key]
  );
  return objCopy;
};
