(ns clj-0MQ.examples.pubsub
  (:use [clj-0MQ.core :as zcore])
  (:use [clj-0MQ.socket :as zsocket]))
   
(defn- string-to-bytes [s] (.getBytes s))
(defn- bytes-to-string [b] (String. b))

(defn handler [socket id msg] 
  (println (str "Subscriber " id ", received message: " (bytes-to-string msg))))

(defn- on-thread [f]
  (doto (Thread. #^Runnable f) 
    (.start)))

(defn make-publisher []
  (let [ctx (zcore/make-context 1 false)
        socket (zcore/make-socket ctx zsocket/PUB)]
    (zsocket/bind socket "tcp://lo:5551")
    socket))

(defn publish [publisher msg]
  (zsocket/send- publisher (string-to-bytes msg)))

(defn start-subscriber [id]
  ; Create subscriber on separate thread so we can interact with REPL when it
  ; blocks.
  (on-thread 
   #(zcore/with-context [ctx 1]
      (zcore/with-socket [socket ctx zsocket/SUB]
        (zsocket/subscribe socket (string-to-bytes ""))
        (zsocket/connect socket "tcp://localhost:5551")
        (while true
          (let [msg (zsocket/recv socket)]
            (handler socket id msg)))))))