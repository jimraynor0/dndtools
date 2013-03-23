on *:CONNECT: {
  echo -s initializing dndUtilScript
  ensureConnection
;  startRespWatchTimer
;  set %du.queue.start 0;
;  set %du.queue.end 0;
}

;on *:DISCONNECT: {
;  echo -s finalizing dndUtilScript
;  stopRespWatchTimer
;  unset %du.*
;}

on *:INPUT:#:{
  if (.* iswm $1) {
    send $1-
  }
}

on $*:TEXT:*:#:{
  if ($1 == .refresh) {
    changeTopic $chan
    return
  }
  if ($regex($1-, ^\.) || $nick == DiceBot || $nick == Oicebot) {
    send $1-
    echo $chan message sent: $1-
  }
}

on $*:TEXT:*:?:{
  if ($regex($1-, ^\.) || $nick == DiceBot || $nick == Oicebot) {
    send $1- $nick
  }
  if ($1 == .refresh) {
    changeTopic $chan
  }
}

alias ensureConnection {
  sockclose duTcpConn
  sockopen duTcpConn 127.0.0.1 50811
}

alias send {
  sockwrite -n duTcpConn $1- $chan $nick
;  if ($nick) {
;    sockudp -k duConn 127.0.0.1 20811 $1- $chan $nick
;  } 
}

on *:sockread:duTcpConn: {
  if ($sockerr) return 
  sockread %du.resp
  tokenize 32 %du.resp
  if (msg == $1) {
     sayTheWord $2 $4-
  }
  if (topic == $1) {
    saveTopic $2 $4-
  }
  if (updateTopic == $1) {
    changeTopic $2
  }
  
  unset %du.resp
}
alias startRespWatchTimer {
  /timerRespWatch -co 0 1 watchResp
}

alias stopRespWatchTimer {
  /timerRespWatch off
}

alias watchResp {
  while (%du.queue.start < %du.queue.end) {
    echo -s entering loop
    echo -s du.queue.start is: %du.queue.start
    echo -s du.queue.end is: %du.queue.end
    inc %du.queue.start
    var %bufName = $+(du.queue.,%du.queue.start)
    echo -s buf name is: %bufName
    echo -s buf value is: $($+(%,%bufName),2)
    if ($($+(%,%bufName),2)) {
      tokenize 32 $($+(%,%bufName),2)
      if (msg == $1) {
        sayTheWord $2 $4-
      }
      if (topic == $1) {
        saveTopic $2 $4-
      }
      if (updateTopic == $1) {
        changeTopic $2
      }
    }
  }
  unset %du.queue.*
  set %du.queue.start 0
  set %du.queue.end 0
}

alias sayTheWord {
  msg $1 $2-
}

alias saveTopic {
  set $+(%,du.topic.,$1) $2-
}

alias changeTopic {
  var %bufName = $+(du.topic.,$1)
  topic $1 $($+(%,%bufName),2)
}

on *:udpRead:duConn: {
  sockread &t
  inc %du.queue.end
  echo -s du.queue.end increased: %du.queue.end
  set $+(%,du.queue.,%du.queue.end) $bvar(&t,1-).text
}
