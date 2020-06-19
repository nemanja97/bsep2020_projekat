export const QueryService = {
  formQuery,
};

function formQuery(queryObject) {
  return new URLSearchParams(removeEmpty(queryObject));
}

const removeEmpty = (obj) => {
  const objCopy = JSON.parse(JSON.stringify(obj));
  Object.keys(objCopy).forEach(
    (key) =>
      (objCopy[key] === null ||
        objCopy[key] === undefined ||
        objCopy[key] === []) &&
      delete objCopy[key]
  );
  return objCopy;
};
