(ns clj-zeromq.socket)

;; Values for flags in Socket's send and recv functions.

(def
  ^{:doc "Socket flag to indicate a nonblocking send or recv mode."}
  NONBLOCK 1)

(def
  ^{:doc ""}
  DONTWAIT 1)

(def
  ^{:doc "Socket flag to indicate that more message parts are coming."}
  SNDMORE  2)

;; Socket types, used when creating a Socket.

(def
  ^{:doc "Flag to specify a exclusive pair of sockets."}
  PAIR 0)

(def
  ^{:doc "Flag to specify a PUB socket, receiving side must be a SUB."}
  PUB 1)

(def
  ^{:doc "Flag to specify the receiving part of the PUB socket."}
  SUB 2)

(def
  ^{:doc "Flag to specify a REQ socket, receiving side must be a REP."}
  REQ 3)

(def
  ^{:doc "Flag to specify the receiving part of a REQ socket."}
  REP 4)

(def
  ^{:doc "Flag to specify a DEALER socket (aka XREQ). DEALER is really a combined ventilator / sink that does load-balancing on output and fair-queuing on input with no other semantics. It is the only socket type that lets you shuffle messages out to N nodes and shuffle the replies back, in a raw bidirectional asynch pattern."}
  DEALER  5)

(def
  ^{:doc "Old alias for DEALER flag.
Flag to specify a XREQ socket, receiving side must be a XREP. Deprecated; as of release 3.0 of zeromq, replaced by DEALER"}
  XREQ DEALER)

(def
  ^{:doc "Flag to specify ROUTER socket (aka XREP). ROUTER is the socket that creates and consumes request-reply routing envelopes. It is the only socket type that lets you route messages to specific connections if you know their identities."}
  ROUTER  6)

(def
  ^{:doc "Old alias for ROUTER flag.
Flag to specify the receiving part of a XREQ socket. Deprecated; as of release 3.0 of zeromq, replaced by ROUTER"}
  XREP ROUTER)

(def
  ^{:doc "Flag to specify the receiving part of a PUSH socket."}
  PULL 7)

(def
  ^{:doc "Flag to specify a PUSH socket, receiving side must be a PULL."}
  PUSH 8)

(def
  ^{:doc "Flag to specify a STREAMER device."}
  STREAMER 1)

(def
  ^{:doc "Flag to specify a FORWARDER device."}
  FORWARDER 2)

(def
  ^{:doc "Flag to specify a QUEUE device."}
  QUEUE  3)


(def
  ^{:doc "@see ZMQ#PULL"}
  UPSTREAM PULL)

(def
  ^{:doc "@see ZMQ#PUSH"}
  DOWNSTREAM PUSH)

(defn close
  "This is an explicit \"destructor\"."
  [socket]
  (.close socket))

(defn get-type
  "Retrieve the socket type for the 'socket'."
  [socket]
  (.getType socket))

(defn get-linger
  "Return the linger period."
  [socket]
  (.getLinger socket))

(defn get-reconnect-ivl
  "Return the reconnect IVL."
  [socket]
  (.getReconnectIVL socket))

(defn get-backlog
  "Return the backlog."
  [socket]
  (.getBacklog socket))

(defn reconnect-ivl-max
  "Return the reconnectIVLMax."
  [socket]
  (.getReconnectIVLMax socket))

(defn get-max-msg-size
  "Return the maxMsgSize."
  [socket]
  (.getMaxMsgSize socket))

(defn get-snd-hwm
  "Return the SndHWM."
  [socket]
  (.getSndHWM socket))

(defn get-rcv-hwm
  "Return the recvHWM period."
  [socket]
  (.getRcvHWM socket))

(defn get-hwm
  "Return the High Water Mark."
  [socket]
  (.getHWM socket))

(defn get-swap
  "Return the number of messages to swap at most."
  [socket]
  (.getSwap socket))

(defn get-affinity
  "Return the affinity."
  [socket]
  (.getAffinity socket))

(defn get-identity
  "Return the Identitiy."
  [socket]
  (.getIdentity socket))

(defn get-rate
  "Return the Rate."
  [socket]
  (.getRate socket))

(defn get-recovery-interval
  "Return the RecoveryIntervall."
  [socket]
  (.getRecoveryInterval socket))

(defn multicast-loop?
  "Return true if socket has multicast loop."
  [socket]
  (.hasMulticastLoop socket))

(defn set-multicast-hops
  "Sets the time-to-live field in every multicast packet sent from this socket. The default is 1 which means that the multicast packets don't leave the local network."
  [socket & [mcast-hops]]
  (.setMulticastHops socket (or (mcast-hops) 1)))

(defn get-multicast-hops
  "Return the Multicast Hops."
  [socket]
  (.getMulticastHops socket))

(defn set-received-timeout
  "Sets the timeout for receive operation on the socket. If the value is 0, recv will return immediately, with a EAGAIN error if there is no message to receive. If the value is -1, it will block until a message is available. For all other values, it will wait for a message for that amount of time before returning with an EAGAIN error."
  [socket tout]
  (.setReceiveTimeOut socket tout))

(defn get-received-timeout
  "Return the Receive Timeout."
  [socket]
  (.getReceiveTimeOut socket))

(defn set-send-timeout
  "Sets the timeout for send operation on the socket. If the value is 0, send will return immediately, with a EAGAIN error if the message cannot be senet. If the value is -1, it will block until the message is sent. For all other values, it will try to send the message for that amount of time before returning with an EAGAIN error."
  [socket tout]
  (.setSendTimeOut socket tout))

(defn get-send-timeout
  "Return the Send Timeout."
  [socket]
  (.getSendTimeOut socket))

(defn get-send-buffer-size
  "Return the kernel send buffer size."
  [socket]
  (.getSendBufferSize socket))

(defn get-received-buffer-size
  "Return the kernel receive buffer size."
  [socket]
  (.getReceiveBufferSize socket))

(defn receive-more?
  "Returns a boolean value indicating if the multi-part message currently being read from the specified 'socket' has more message parts to follow. If there are no message parts to follow or if the message currently being read is not a multi-part message a value of zero shall be returned. Otherwise, a value of 1 shall be returned."
  [socket]
  (.hasReceiveMore socket))

(defn get-fd
  "The 'ZMQ_FD' option shall retrieve file descriptor associated with the 0MQ socket. The descriptor can be used to integrate 0MQ socket into an existing event loop. It should never be used for anything else than polling -- such as reading or writing. The descriptor signals edge-triggered IN event when something has happened within the 0MQ socket. It does not necessarily mean that the messages can be read or written. Check ZMQ_EVENTS option to find out whether the 0MQ socket is readable or writeable."
  [socket]
  (.getFD socket))

(defn get-events
  "The 'ZMQ_EVENTS' option shall retrieve event flags for the specified socket. If a message can be read from the socket ZMQ_POLLIN flag is set. If message can be written to the socket ZMQ_POLLOUT flag is set."
  [socket]
  (.getEvents socket))

(defn set-linger
  "Sets the period for pending outbound messages to linger in memory after closing the socket. Value of -1 means infinite. Pending messages will be kept until they are fully transferred to the peer. Value of 0 means that all the pending messages are dropped immediately when socket is closed. Positive value means number of milliseconds to keep trying to send the pending messages before discarding them."
  [socket linger]
  (.setLinger socket linger))

(defn set-reconnect-ivl
  [socket ivl]
  (.setReconnectIVL socket ivl))

(defn set-backlog
  [socket backlog]
  (.setBacklog socket backlog))

(defn set-reconnect-ivl-max
  [socket ivl-max]
  (.setReconnectIVLMax socket ivl-max))

(defn set-max-msg-size
  [socket size]
  (.setMaxMsgSize socket size))

(defn set-snd-hwm
  [socket hwm]
  (.setSndHWM socket hwm))

(defn set-rcv-hwm
  [socket hwm]
  (.setRcvHWM socket hwm))

(defn set-hwm
  "The 'ZMQ_HWM' option shall set the high water mark for the specified 'socket'. The high water mark is a hard limit on the maximum number of outstanding messages 0MQ shall queue in memory for any single peer that the specified 'socket' is communicating with. If this limit has been reached the socket shall enter an exceptional state and depending on the socket type, 0MQ shall take appropriate action such as blocking or dropping sent messages. Refer to the individual socket descriptions in the man page of zmq_socket[3] for details on the exact action taken for each socket type."
  [socket hwm]
  (.setHWM socket hwm))

(defn set-swap
  "Set the disk offload (swap) size for the specified 'socket'. A socket which has 'swap' set to a non-zero value may exceed its high water mark; in this case outstanding messages shall be offloaded to storage on disk rather than held in memory."
  [socket swap]
  (.setSwap socket swap))


(defn set-affinity
  "Set the I/O thread affinity for newly created connections on the specified 'socket'. Affinity determines which threads from the 0MQ I/O thread pool associated with the socket's _context_ shall handle newly created connections. A value of zero specifies no affinity, meaning that work shall be distributed fairly among all 0MQ I/O threads in the thread pool. For non-zero values, the lowest bit corresponds to thread 1, second lowest bit to thread 2 and so on. For example, a value of 3 specifies that subsequent connections on 'socket' shall be handled exclusively by I/O threads 1 and 2."
  [socket affinity]
  (.setAffinity socket))

(defn set-identity
  "Set the identity of the specified 'socket'. Socket identity determines if existing 0MQ infastructure (_message queues_, _forwarding devices_) shall be identified with a specific application and persist across multiple runs of the application. If the socket has no identity, each run of an application is completely separate from other runs. However, with identity set the socket shall re-use any existing 0MQ infrastructure configured by the previous run(s). Thus the application may receive messages that were sent in the meantime, _message queue_ limits shall be shared with previous run(s) and so on. Identity should be at least one byte and at most 255 bytes long. Identities starting with binary zero are reserved for use by 0MQ infrastructure."
  [socket identity]
  (.setIdentity socket))

(defn subscribe
  "Establishes a new message filter on a 'ZMQ_SUB' socket. Newly created 'SUB' sockets shall filter out all incoming messages, therefore you should call this option to establish an initial message filter. An empty 'option_value' of length zero shall subscribe to all incoming messages. A non-empty 'option_value' shall subscribe to all messages beginning with the specified prefix. Mutiple filters may be attached to a single 'SUB' socket, in which case a message shall be accepted if it matches at least one filter."
  [socket topic]
  (.subscribe socket topic))

(defn unsubscribe
  "Removes an existing message filter on a 'SUB' socket. The filter specified must match an existing filter previously established with the 'ZMQ_SUBSCRIBE' option. If the socket has several instances of the same filter attached the 'ZMQ_UNSUBSCRIBE' option shall remove only one instance, leaving the rest in place and functional."
  [socket topic]
  (.unsubscribe socket topic))

(defn set-rate
  "Set the maximum send or receive data rate for multicast transports such as  in the man page of zmq_pgm[7] using the specified 'socket'."
  [socket rate]
  (.setRate socket rate))

(defn set-recovery-interval
  "Set the recovery interval for multicast transports using the specified 'socket'. The recovery interval determines the maximum time in seconds that a receiver can be absent from a multicast group before unrecoverable data loss will occur. CAUTION: Excersize care when setting large recovery intervals as the data needed for recovery will be held in memory. For example, a 1 minute recovery interval at a data rate of 1Gbps requires a 7GB in-memory buffer."
  [socket ivl]
  (.setRecoveryInterval socket ivl))

(defn set-multicast-loop
  "Controls whether data sent via multicast transports using the specified 'socket' can also be received by the sending host via loopback. A value of zero disables the loopback functionality, while the default value of 1 enables the loopback functionality. Leaving multicast loopback enabled when it is not required can have a negative impact on performance. Where possible, disable 'ZMQ_MCAST_LOOP' in production environments."
  [socket mcast-loop]
  (.setMulticastLoop socket mcast-loop))

(defn set-send-buffer-size
  "Set the underlying kernel transmit buffer size for the 'socket' to the specified size in bytes. A value of zero means leave the OS default unchanged. For details please refer to your operating system documentation for the 'SO_SNDBUF' socket option."
  [socket size]
  (.setSendBufferSize socket size))

(defn set-receive-buffer-size
  "Set the underlying kernel receive buffer size for the 'socket' to the specified size in bytes. A value of zero means leave the OS default unchanged. For details refer to your operating system documentation for the 'SO_RCVBUF' socket option."
  [socket size]
  (.setReceiveBufferSize socket size))

(defn bind
  "Bind to network interface. Start listening for new connections."
  [socket addr]
  (.bind socket addr))

(defn connect
  "Connect to remote application."
  [socket addr]
  (.connect socket addr))

(defn send-
  "Send a message as an array of bytes. The flags apply to the send operation. Returns true if send was successful, false otherwise."
  [socket msg & flags]
  (.send socket msg (if flags (apply bit-xor flags) 0)))

(defn recv
  "Receive a message. The flags apply to the receive operation. Returns the message received, as an array of bytes; null on error."
  [socket & flags]
  (.recv socket (if flags (apply bit-xor flags) 0)))

(defn recv-to-buffer
  "Receive a message in to a specified buffer. Buffer is byte[] to copy zmq message payload in to. Offset specifies offset in buffer and len specifies max bytes to write. If len is smaller than the incoming message size, the message will be truncated. The flags to apply to the receive operation. Returns the number of bytes read, -1 on error."
  [socket buffer offset len & flags]
  (.recv socket buffer offset len (if flags (apply bit-xor flags) 0)))
