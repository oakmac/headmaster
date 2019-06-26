const R = require('ramda')

function compareIncomingAndExistingItems(incoming, existing) {
  const existingItems = R.map(R.pick(['id']))(existing)
  const [
    itemsToRelate,
    itemsToInsert,
  ] = R.pipe(
    // what items are in the incoming array but missing from the current items?
    R.difference(incoming),
    // filter items on with and without ids
    R.partition(R.has('id')),
  )(existingItems)

  // items that are in the existing but missing from incoming should be un-related.
  const itemsToUnlink = R.difference(existingItems, R.map(R.pick(['id']))(incoming))

  return {
    itemsToRelate,
    itemsToInsert,
    itemsToUnlink,
  }
}

module.exports = {
  compareIncomingAndExistingItems,
}