(ns clj-0MQ.core
  (:import (org.zeromq ZMQ ZContext))
  (:import (java.util.concurrent CopyOnWriteArrayList)))



(defn make-context [io-threads linger &{:keys [context main]
                                        :or { context nil
                                             main true}}]
  (doto
      (ZContext.)
    (. setIoThreads io-threads)
    (. setLinger linger)
    (. setMain main)
    (. setContext context)))

(defmacro with-context 
  "Constructs a 0MQ context with the given `name` and evaluates the exprs with that context in the lexical scope.  The exprs are contained in an implicit `do`.  The context is finally destroyed.
  (with-context [ctx 1 1 0] ... )"
  [[name- app-threads io-threads & flags] & body]
  `(let [~name- (make-context ~app-threads ~io-threads ~@flags)]
       ~@body))

(defn make-socket [context socket-type]
  (.createSocket context socket-type))

(defmacro with-socket
  "Constructs a 0MQ socket with the given `name` and `context`.  The exprs are evaluated with that socket in the lexical scope.  The exprs are contained in an implicit `do`.  The socket is finally destroyed.
(with-socket [sock ctx +req+] ... )
(with-socket [sock ctx +pub+] ... )"
  [[name- context socket-type] & body]
  `(let [~name- (make-socket ~context ~socket-type)]
     (try
       ~@body
       (finally (.close ~name-)))))
