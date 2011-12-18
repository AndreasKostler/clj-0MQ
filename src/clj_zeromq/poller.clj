(ns clj-zeromq.poller)

;; These values can be ORed to specify what we want to poll for.
(def POLLIN 1)
(def POLLOUT 2)
(def POLLERR 4)
(def SIZE-DEFAULT 32)

(defn make-poller
  "Create a new Poller within this context, with a default size."
  ([context] (make-poller context SIZE-DEFAULT))
  ([context size] (.poller context context size)))

(defn register
  "Register a Socket for polling on the specified events. The event mask is composed by XORing POLLIN, POLLOUT, and POLLERR. If no event-mask is given, register a socket for polling on all events. Returns the index identifying this Socket in the poll set."
  ([poller socket]
     (.register poller socket (bit-or POLLIN POLLOUT POLLERR)))
  ([poller socket event-mask]
     (.register poller socket event-mask)))

(defn ungregister
  "Unregister a Socket for polling on the specified events."
  [poller socket]
  (.unregister poller socket))

(defn get-socket
  "Get the socket associated with an index. Returns the Socket associated with that index (or null)."
  [poller idx]
  (.getSocket poller idx))

(defn get-timeout
  "Get the current poll timeout. Returns the current poll timeout in microseconds. Deprecated; Timeout handling has been moved to the poll() methods"
  [poller]
  (.getTimeout poller))

(defn set-timeout
  "Set the poll timout.
   @param timeout the desired poll timeout in microseconds.
   @deprecated Timeout handling has been moved to the poll() methods."
  [poller tout]
  (.setTimeout poller tout))
       
(defn get-size
  "Get the current poll set size."
  [poller]
  (.getSize poller))

(defn get-next
  "Get the index for the next position in the poll set size."
  [poller]
  (.getNext poller))

(defn poll
  "Issue a poll call. If the poller's internal timeout value has been set, use that value as timeout; otherwise, block indefinitely. Returns how many objects where signalled by poll."
  [poller]
  (.poll poller))

(defn poll
  "Issue a poll call, using the specified timeout value. Since ZeroMQ 3.0, the timeout parameter is in <i>milliseconds<i>, but prior to this the unit was <i>microseconds</i>. If tout = -1, it will block indefinitely until an event happens; if tout = 0, it will return immediately; otherwise, it will wait for at most that many milliseconds/microseconds (see above). Returns how many objects where signalled by poll."
  [poller tout]
  (.poll poller tout))

(defn pollin?
  "Check whether the specified element in the poll set was signalled for input. Returns true if the element was signalled."
  [poller idx]
  (.pollin poller idx))

(defn pollout?
  "Check whether the specified element in the poll set was signalled for output. Returns true if the element was signalled."
  [poller idx]
  (.pollout poller idx))

(defn pollerr?
  "Check whether the specified element in the poll set was signalled for error. Returns true if the element was signalled."
  [poller idx]
  (.pollerr poller idx))