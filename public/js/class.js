;(function () {
  var $ = window.Zepto

  function clickNavTab () {

  }

  function getHash () {
    return window.location.href.replace(/^.+#/, '')
  }

  function hideAllSections () {
    $('#sectionTabs').hide()
  }

  function showTab (route) {

  }

  function onHashChange () {
    var route = getHash()
  }

  function addEvents () {
    // $('#sectionTabs .tab').on('click', clickNavTab)
    window.onhashchange = onHashChange;
  }

  function init () {
    addEvents()
  }

  init()
})()
