// javascript-rcopia \ html \ javascript \ framework-rcopia.js
/**
 * @fileOverview Script containing the DrFirst JavaScript framework specific to Rcopia.
 *
 * @author mkotelba
 */

/*===========================================================
 = TIMEOUTTIMER
 ===========================================================*/

var TimeoutTimer = Class.extend(Class.create("TimeoutTimer"), Timer);

Object.extend(TimeoutTimer,
/** @lends TimeoutTimer */
{
	/**
	 * Pattern for URL paths in the member's area.
	 *
	 * @constant
	 * @type RegExp
	 */
	MEMBERS_AREA_PATH_PATTERN: /^\/members\/.+$/,

	/**
	 * TODO: document
	 *
	 * @constant
	 * @type String
	 */
	DEFAULT_TIMEOUT_URL: "/login.jsp?err=343",

	/**
	 * TODO: document
	 *
	 * @constant
	 * @type String
	 */
	TIMEOUT_ACTION_URL: "/servlet/RcopiaWebServlet?screen=BlankScreen&action=time_out"
});

Object.extend(TimeoutTimer.prototype,
/** @lends TimeoutTimer.prototype */
{
	/**
	 * URL to redirect to on timeout.
	 *
	 * @type String
	 */
	timeoutURL: undefined,

	/**
	 * Whether this page is a popup.
	 *
	 * @type Boolean
	 */
	isPopup: undefined,

	/**
	 * TODO: document
	 *
	 * @type AjaxRequest
	 */
	rcopiaTimeoutRequest: undefined,

	/**
	 * TODO: document
	 *
	 * @type Function
	 */
	userTimeoutCallback: undefined,

	/**
	 * @class Timer for timing-out Rcopia sessions.
	 * @augments Timer
	 * @exports TimeoutTimer#__constructs as TimeoutTimer
	 *
	 * @param {Number} timeoutInterval timeout interval to use
	 * @param {String} timeoutURL timeout URL to use
	 * @param {String} isPopup whether this page is a popup
	 */
	__construct: function (timeoutInterval, timeoutURL, isPopup)
	{
		timeoutInterval *= 1000;

		this.timeoutURL = timeoutURL.trim().isEmpty() ? TimeoutTimer.DEFAULT_TIMEOUT_URL : timeoutURL;
		this.isPopup = Boolean.parse(isPopup);

		window.timeoutTimer = this;

		this.__superConstruct(this.timeoutUser.bind(this), timeoutInterval);
	},

	/**
	 * TODO: document
	 */
	onRcopiaTimeoutReadyStateChange: function (event)
	{
		if (!window.closed)
		{
			var request = this.rcopiaTimeoutRequest.getRequest();

			console.debug("Rcopia timeout ready state change: readyState=" + request.readyState + (!Browser.isIE() ? ", state=" + request.state : ""));

			if ((request.readyState == AjaxRequestReadyState.LOADED.getReadyState()) &&
				(Browser.isIE() || (request.status != AjaxRequestStatus.NOT_FOUND.getStatus())))
			{
				console.info("Rcopia timeout action loaded - calling user timeout callback ...");

				closePopups();

				this.userTimeoutCallback();
			}
		}
	},

	/**
	 * Performs a timeout in Rcopia.
	 *
	 * @param {Function} userTimeoutCallback user callback function once the rcopia timeout action is loaded
	 */
	timeoutRcopia: function (userTimeoutCallback)
	{
		if (!window.closed)
		{
			console.info("Sending Rcopia timeout action to: " + TimeoutTimer.TIMEOUT_ACTION_URL);

			this.userTimeoutCallback = userTimeoutCallback.bind(this);

			this.rcopiaTimeoutRequest = new AjaxRequest(TimeoutTimer.TIMEOUT_ACTION_URL,
			{
				onReadyStateChange: this.onRcopiaTimeoutReadyStateChange.bind(this)
			});

			this.rcopiaTimeoutRequest.send();
		}
	},

	/**
	 * Performs a timeout for the user.
	 */
	timeoutUser: function ()
	{
		if (!window.closed)
		{
			console.info("User has timed out after " + this.interval + " millisecond[s] of inactivity ...");

			if (this.isPopup)
			{
				var membersAreaOpener = false;

				try
				{
					membersAreaOpener = Object.exists(window.opener) && Object.exists(window.opener.document.getLocation) &&
						TimeoutTimer.MEMBERS_AREA_PATH_PATTERN.test(new URL(window.opener.document.getLocation()).getPath());
				}
				catch (e)
				{
					console.debug(e);
				}

				if (membersAreaOpener)
				{
					console.info("Redirecting timed-out members area to: " + this.timeoutURL);

					window.opener.document.redirect(this.timeoutURL);

					this.timeoutRcopia(function ()
					{
						window.open("", "_self", "");
						window.close();
					});
				}
				else
				{
					console.info("Attempting to close this timed-out Rcopia popup ...");

					this.timeoutRcopia(function ()
					{
						window.open("", "_self", "");
						window.close();

						console.info("Timed-out Rcopia popup closing failed - redirecting to: " + this.timeoutURL);

						document.location.href = this.timeoutURL;
					});
				}
			}
			else
			{
				console.info("Redirecting timed-out Rcopia page to: " + this.timeoutURL);

				this.timeoutRcopia(function ()
				{
					document.location.href = this.timeoutURL;
				});
			}
		}
	},

	initTimer: function ()
	{
		this.__super.prototype.initTimer.call(this);

		console.info("Initialized timeout timer: id=" + this.id + ", interval=" + this.interval + ", timeoutURL=" + this.timeoutURL + ", isPopup=" +
			this.isPopup);
	}
});