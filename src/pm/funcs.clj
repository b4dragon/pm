(ns pm.funcs
  (:require [pm.read-json :as json]
            [clojure.inspector :as ins]))


(ins/inspect mip-addr)
(defn raw-protocol
  "return protocol that read from file"
  [root-f strcut-f]
  (let [protocol (json/read-protocol root-f)
        struct (json/read-json struct-f)]
    {:protocol protocol :struct struct}))
    
(defn update-protocol
  [c]
  (let [{:keys [protocol struct]} c]
    ;;update trig type - MIP_ADDR_CTRL_SETTER
    ;; update event format - MIP_ADDR_EVENT_FORMAT
    ;;buf addr - MIP_ADDR_IMAGE_INFO
    ;;support image - MIP_ADDR_IMAGE_CONTROL
    ;;update log buf - MIP_ADDR_LOG_INFO
    ;;update - MIP_ADDR_CUSTOM_PROTOCOL_INFO
    ;;and update size. 

    {:protocol protocol :struct struct}
    ))
  
(defn decode-cmd [req protocol]
  )

(defn encode-to-am [req protocol]
  )

(defn decode-to-ui [req protocol]
  )




(defn add-size [itm st]
  (let [count (:count itm)
        type (:type itm)]


(def primitive-size
  {:int8_t 1
   :uint8_t 1
   :int16_t 2
   :uint16_t 2
   :int32_t 4
   :uint32_t 4
   :char 1
   :bool 1})

;;only use last value when check enum size
(defn search-enum-last-value [type]
  (let [enum-name (-> type
                      (clojure.string/split #"\s+")
                      (second))
        enum-info (first (filter #(= (:name %) enum-name) (:structure mip)))]
    (-> enum-info
        (:value)
        (last)
        (:value))))

;;consume enum val is unsigned value. there isn't error exception
(defn enum-size [type]
  (let [last-val (search-enum-last-value type)]
  (cond
    (<= last-val 256) 1
    (<= last-val 65536) 2
    :else 4)))

(defn type-size [type]
  "get a size of primitive, enum, struct"
  (let [enum
  (if-let[sz ((keyword type) primitive-size)]
    sz
    (enum-size type)))
 
(defn each-itm-size [itm]
  (let [itm-struct (:structure itm)
        type-size (->> itm-struct
                       (map :type)
                       (map type-size))
        count (map :size itm-struct)]
    (map * type-size count)))

(defn whole-itm-size
  [itm]
  (reduce + (each-itm-size itm)))


(defn make-packet-to-am
  "make a packet to am
  return: read+=size+=subaddr"
  [v]
  (->> v
       (interpose "-+")
       (apply str)))

;;init
;;read

;;MIP_ADDR_INFO_INTERFACE
;;MIP_ADDR_INFO_VERSION
;;MIP_ADDR_INFO_SECTION_ADDR
;;MIP_ADDR_INFO_INTEGRITY
;;MIP_ADDR_INFO_EXTRA
;;MIP_ADDR_CONFIDENTIAL_INFO
;;MIP_ADDR_INFO_PANEL
;;MIP_ADDR_CTRL_SETTER
;;MIP_ADDR_EVENT_FORMAT
;;MIP_ADDR_IMAGE_INFO
;;MIP_ADDR_IMAGE_CONTROL
;;MIP_ADDR_LOG_INFO
;;MIP_ADDR_LOG_CONTROL
;;MIP_ADDR_CUSTOM_PROTOCOL_INFO

;;status


(map clojure.pprint/pprint mip)

(defn send-to-am [v]
  (let [packet (make-packet-to-am v)]
    
  ))

(defn recevice-from-am []
  ;;wait until get a msg
  ;;decode it
  
  )

(defn read
  [ele protocol-map]
  (let [target (ele protocol-map)
        size (itm-whole-size target)]
    (send-to-am ["read" size (:lsb_addr target)])
    (receive-from-am)))

(defn read-mip [ele]
  (read ele mip))
    

(defn read-intr []
  (send-to-am ["intr"])
  (receive-from-am))
  
;;read trigger state that depend on intr, reg, none.
;;only read there isnt' image and on log mode
(defn read-trig
  []
  (let [intr (read-intr)
        ctrl-setter (read-mip :ctrl_setter)
        ctrl-getter (read-mip :ctrl_getter)]
    (if (= (:evt_ready ctrl-setter) :ETT_INTERRUPT)
      (assoc ctrl-getter :evt_ready (:intr)))))

;;check trig type
;;need a read intr signal depedn on ett ex, intr, reg, none
(defn init
  []
  )


;; (defn read
;;   [ch-to-am ch-from-am sub-addr]
;;   (let [size (get-subaddr-size sub-addr)
;;         packet (make-packet-to-am ["read" size sub-addr])]
;;     (>!! ch-to-ma packet)
;;     (loop [] 
;;       (when-let [rsp (<!! ch-from-am)]
;;         rsp)
;;       (recur))))

;;read image 



(defn read-image []
  ;;read
  ;;MIP_ADDR_IMAGE_DATA_FORMAT
  ;;MIP_ADDR_IMAGE_FINGER_NUM
  ;;MIP_ADDR_IMAGE_FINGER_AREA
  ;;read image scalar or matrix  - MIP_ADDR_GENERAL_BUFFER
  
  (set-image-type :IT-NONE)
  )

(defn read-log []
  ;;read
  ;;MIP_ADDR_LOG_DATA_FORMAT
  ;;"0x6000
  ;;check "---"
  ;;?? - extended_log_flags
  ;;read-log
  ;;read - "0x2000 - scalar or matrix
  ;;
  )

(defn loop []
  (check-trig)
      )




(defn from-ui-to-am [item]
  (let [filtered nil]
    filtered))

(defn from-am-to-ui [item]
  (let [filtered nil]
    filtered))

