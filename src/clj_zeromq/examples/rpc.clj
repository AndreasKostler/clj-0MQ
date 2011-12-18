(ns clj-zeromq.examples.rpc
  (:use [clj-zeromq.core :as zmq]
        [clj-zeromq.socket :as zsocket]))

(defn- string-to-bytes [s] (.getBytes s))
(defn- bytes-to-string [b] (String. b))

(defn handler [socket query]
  (let [query (bytes-to-string query)
        resultset (str "Received query: " query)]
    (zsocket/send- socket (string-to-bytes resultset))))

(defn- on-thread [f]
  (doto (Thread. #^Runnable f)
    (.start)))

(defn start-server []
  (let [ctx (zmq/make-context 1 1)
        socket (zmq/make-socket ctx zsocket/REP)]
    (on-thread
     #(do
        (zsocket/bind socket "tcp://lo:5555")
        (while true
          (let [query (zsocket/recv socket)]
            (handler socket query)))))))

(defn send-to-server [query]
  (zmq/with-context [ctx 1 1]
    (zmq/with-socket [socket ctx zsocket/REQ]
      (zsocket/connect socket "tcp://localhost:5555")
      (zsocket/send- socket (string-to-bytes query))
      (println (str "Received response: " (bytes-to-string (zsocket/recv socket)))))))