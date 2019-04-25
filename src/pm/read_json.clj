(ns pm.read-json
  (:require [clojure.data.json :as json]
            [clojure.inspector :as ins]
            [clojure.java.io :as io]))

(defrecord protocol-itm [addr struct-name vari-name type count size])


(defn make-path [fname]
  (let [d-path "./mip_json/"
        extention ".json"]
    (str d-path fname extention )))

(defn read-json
  [fname]
  (let [f-path (make-path fname)]
    (-> f-path
        (slurp)
        (json/read-str :key-fn keyword))))


;; (defn hex-to-int
;;   "ex: 0x11 or 0x1111"
;;   [^String s]
;;   (let [digit (->> s
;;                   (partition 2)
;;                   (rest)
;;                   (map #(apply str %))
;;                   (apply str))]
;;     (Integer/parseInt digit 16)))


(defn add-msb [val]
  (let [msb-digit "10"
        lsb-digit (->> val
                       (partition 2)
                       (second)
                       (apply str))]
    (str "0x" msb-digit lsb-digit)))

(defn search
  [msb-itm]
  (let [{:keys [msb_name msb_addr]} msb-itm
        content (read-json msb_name)
        msb-digit (->> msb_addr
                       (partition 2)
                       (second)
                       (apply str))]
    (letfn [(add-msb [val]
              (let [lsb-digit (->> val
                                  (partition 2)
                                  (second)
                                  (apply str))]
                (str "0x" msb-digit lsb-digit)))
            (convert-map [itm]
              (assoc itm :lsb_addr (add-msb (:lsb_addr itm))))]
    (loop [re-make {}
           c content]
      (if (seq c)
        (recur (assoc re-make ((comp keyword :name) (first c)) (convert-map (first c))) (rest c))
        re-make)))))

(defn digit-from-hex [hex]
  (->> hex
       (partition 2)
       (second)
       (apply str)))

(defn make-record
  [itm]
  (let [k (key itm)
        s-name (:name (val itm))
        msb (->> (:lsb_addr (val itm))
                 (partition 2)
                 (second)
                 (apply str))
        v (:structure (val itm))]
    (map #(protocol-itm.
           (str "0x" msb (digit-from-hex (:lsb_addr %)))
           s-name
           (:name %)
           (:type %)
           (:size %)
           0) v)))


;; (defn read-protocol [root struct]
;;   (let [msb-fname root
;;         msb (read-json msb-fname)
;;         lsb (->> msb
;;                  (map search)
;;                  (apply merge))
;;         ext-struct (->> struct
;;                         (read-json))]
;;     (merge lsb {(keyword struct) ext-struct})))
    

(defn read-protocol
  [root-file]
  (let [msb (read-json root-file)
        lsb (->> msb
                 (map search)
                 (apply merge))]
    (->> lsb
         (map make-record)
         (flatten))))


(defn decode-packet [packet-format ^String packet]
  (let [size (each-itm-size packet-format)
        names (map :name packet-format)]
    (loop [acc []
           p packet
           s size
           n names]
      (if (seq p)
        (let [[cur left] (split-at (first s) p)]
          (recur (conj acc [(first n) (apply str cur)]) left (rest s) (rest n)))
        acc))))


;; (decode-packet (:structure (:panel_information mip)) "abcdefghijklmnopqrstuvwxyzaaaa")

