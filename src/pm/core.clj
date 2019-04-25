(ns pm.core
  (:require [aleph.http :as http]
            [manifold.stream :as s]
            [cognitect.transit :as t]
            [pm.server :as server]
            [pm.funcs :as funcs]
            [pm.am-client :as ac]))
            
;;testing server
(def ws @(http/websocket-client "ws://0.0.0.0:8080"))
@(s/put! ws "read-+8-+288")
(def a @(s/take! ws))
(close ws)



;;connect am as a client.
;;make server and connect gui.
;;should be replace with config file

(def am-addr "ws://0.0.0.0:8080")
(def pm-addr "ws://0.0.0.0:8081")

;; (defn -main []

;;   (let [{:keys [from-am to-am]} (ac/connect-server am-addr)
;;         {:keys [from-ui to-ui]} (server/start-server pm-addr)]
;;     (let-fn [(put-to-am
;;               "just send"
;;               [c ch-to-am]
;;               )
;;              (take-from-am
;;               "wait until get a rsp"
;;               [ch-from-am]
;;               )]
;;       ;;if ws is connect to am
;;       ;;than read protocol
;;       ;;after that update
;;       (let [raw-protocol (raw-protocol "protocol_msb" "struct")
;;             protocol (update-protocol raw-protocol)]
       
;;         ;;make a pipeline
;;         ;;(pipeline 1 to-am funcs/from-ui-to-am from-ui)
;;         ;;(pipeline 1 to-ui funcs/from-am-to-ui from-am)
;;         ;;should be sequential..
        
;;         (while true
;;           (funcs/read-trig)
;;           (when event-ready
;;             (funcs/read-packet-or-alert))
;;           (when std.stat
;;             (funcs/read-log-or-image))
;;           (when exist-input-cmd)
;;             (funcs/enable-log)
;;             (recur))
        
;;         (funcs/exit)
;;   )
