const R = require('ramda')

function compareIncomingAndExistingItems(incoming, existing) {
  const [
    itemsToUpdate,
    itemsToInsert,
  ] = R.pipe(
    // what items are in the incoming array but missing from the current items?
    R.difference(incoming),
    // filter items on with and without ids
    R.partition(R.has('id')),
  )(existing)

  // items that are in the existing but missing from incoming should be un-related.
  const itemsToUnlink = R.difference(existing, incoming)

  return {
    itemsToUpdate,
    itemsToInsert,
    itemsToUnlink,
  }
}

module.exports = {
  compareIncomingAndExistingItems,
}