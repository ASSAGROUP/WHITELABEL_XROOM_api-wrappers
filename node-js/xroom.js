class Xroom {

  /**
   * Constructor
   *
   * @param {string} username
   * @param {string} apiKey
   */
  constructor (username, apiKey) {
    this.username = username
    this.apiKey = apiKey
  }

  /**
   * Make an API call
   *
   * @param {string} endpoint
   * @param {string} method
   * @param {any|undefined} payload
   */
  async call (endpoint, method, payload = undefined) {
    // TODO
  }
}

module.exports = { Xroom }
