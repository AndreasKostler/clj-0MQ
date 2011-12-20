(ns clj-0MQ.core
  (:import (org.zeromq ZMQ ZMQ$Context ZContext))
  (:import (java.util.concurrent CopyOnWriteArrayList)))

;; Functions for creating ZMQ contexts. The jzmq library
;; offers the ZContext class which wraps ZMQ/Context and
;; manages its associated sockets. It is prefereable to use
;; managing contexts when possible.
(defmulti make-context (fn [_ x] (identity x)))

(defmethod make-context true
  "Createas a managing ZContext instance."
  [threads managing?]
  (doto (ZContext.) (.setIoThreads threads)))

(defmethod make-context false [threads managing?]
  "Creates an unmanaging ZMQ/Context instance."
  (ZMQ/context threads))

(defmulti destroy-context class)

(defmethod destroy-context ZContext
  "Closes all its sockets and terminates the context (if on main thread)."
  [cxt]
  (.destroy cxt))

(defmethod destroy-context ZMQ$Context [cxt]
  "Terminates the context."
  (.term cxt))

(defmulti make-socket (fn[x _] (class x)))

(defmethod make-socket ZContext
  "Creates a managed socket."
  [cxt type]
  (.createSocket cxt type))

(defmethod make-socket ZMQ$Context [cxt type]
  "Creates an unmanaged socket."
  (.socket cxt type))

(defmulti destroy-socket (fn [x _] (class x)))

(defmethod destroy-socket ZContext
  "The managing context destroys the socket."
  [cxt socket]
  (.destroySocket cxt socket))

(defmethod destroy-socket ZMQ$Context
  "Closes the unmanaged socket."
  [cxt socket]
  (doto socket
    (.setLinger (.getLinger cxt))
    (.close)))

(defmacro with-context
  "Constructs a 0MQ context with the given \"name\" and number of (io)\"threads\" and evaluates the exprs with that context in the lexical scope. An optional :managing? flag can be set to indicate whether a managing context (default) should be created. The context is finally destroyed."
  [[name- threads & {:keys [managing?] :or {managing? true}}] & body]
  `(let [~name- (make-context ~threads ~managing?)]
     (try
       ~@body
       (finally (destroy-context ~name-)))))

(defmacro with-socket
  "Constructs a 0MQ socket with the given \"name\" and \"context\".  The exprs are evaluated with that socket in the lexical scope.  The socket is finally destroyed."
  [[name- cxt socket-type] & body]
  `(let [~name- (make-socket ~cxt ~socket-type)]
     (try
       ~@body
       (finally (destroy-socket ~cxt ~name-)))))