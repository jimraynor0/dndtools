alias createTestCase {
  inc %du.testcase.index
  set $+(%,du.testcase.,%du.testcase.index) $1-
}

alias setupTestCases {
  set %du.testcase.index 0
  setupEnv
  testRoundAndTurn
  testStateAutoBehavior
  testStateAutoBehaviorWithDelay
  testApAndSurge
  testDelay
  testState
  testHp
}

alias setupEnv {
  createTestCase .startgame test
  createTestCase .startbattle
  createTestCase .init g1 1
  createTestCase .init 10
  createTestCase .init OGRE 15
  createTestCase .t init hanbak 20
}

alias testRoundAndTurn {
  createTestCase .surprise
  createTestCase .pre
  createTestCase .t undo
  createTestCase .t undo
  createTestCase .start
  createTestCase .end
  createTestCase .end
  createTestCase .end
  createTestCase .end
  createTestCase .end
  createTestCase .t undo
  createTestCase .t undo
  createTestCase .t undo
  createTestCase .t undo
  createTestCase .t undo
}

alias testStateAutoBehavior {
  createTestCase .start
  createTestCase .t +slow g1
  createTestCase .t +slow han
  createTestCase .t +prone|sont han
  createTestCase .end
  createTestCase .t +slow|sv han
  createTestCase .end
  createTestCase .t +dot5 han
  createTestCase .end
  createTestCase .t +dot5|eont han
  createTestCase .t +dot5|eont g1
  createTestCase .end
  createTestCase .end
  createTestCase .end
  createTestCase .end
  createTestCase .end
  createTestCase .end
  createTestCase .end
  createTestCase .end
  createTestCase .t undo
  createTestCase .t undo
  createTestCase .t undo
  createTestCase .t undo
  createTestCase .t undo
}

alias testStateAutoBehaviorWithDelay {
  createTestCase .start
  createTestCase .t +slow g1
  createTestCase .t +slow han
  createTestCase .t +prone|sont han
  createTestCase .end
  createTestCase .t +slow
  createTestCase .t +slow ogre
  createTestCase .t +slow|sv han
  createTestCase .end
  createTestCase .t +dot5 han
  createTestCase .t +dot4
  createTestCase .t +dot3 g1
  createTestCase .t +dot2 ogre
  createTestCase .end
  createTestCase .t +dot5|eont han
  createTestCase .t +dot5|eont g1
  createTestCase .end
  createTestCase .end
  createTestCase .t after ogre g1
  createTestCase .t before han
}

alias testApAndSurge {
  createTestCase .ap
  createTestCase .ap 2
  createTestCase .charstat
  createTestCase .ap han
  createTestCase .ap han 2
  createTestCase .charstat han
  createTestCase .ap -1 han
  createTestCase .ap -1
  createTestCase .charstat
  createTestCase .undo
  createTestCase .undo
  createTestCase .undo
  createTestCase .charstat
  createTestCase .undo
  createTestCase .undo
  createTestCase .undo
  createTestCase .charstat
  createTestCase .surge
  createTestCase .surge 2
  createTestCase .charstat $nick
  createTestCase .surge han
  createTestCase .surge han 2
  createTestCase .charstat
  createTestCase .surge -1 han
  createTestCase .surge -1
  createTestCase .charstat
  createTestCase .undo
  createTestCase .undo
  createTestCase .undo
  createTestCase .charstat
  createTestCase .undo
  createTestCase .undo
  createTestCase .undo
  createTestCase .charstat
}

alias testDelay {
  createTestCase .go g1 2
  createTestCase .t + G2
  createTestCase .charstat
  createTestCase .t before g2 g1
  createTestCase .charstat
  createTestCase .t after o g2
  createTestCase .charstat
  createTestCase .t before han
  createTestCase .charstat
  createTestCase .t after ogre
  createTestCase .charstat
  createTestCase .t rename g2 G5
  createTestCase .charstat
  createTestCase .t - g5
  createTestCase .charstat
  createTestCase .t after o h
  createTestCase .charstat
  createTestCase .t after g o
  createTestCase .charstat
  createTestCase .t before han g1
  createTestCase .charstat
  createTestCase .t undo
  createTestCase .t undo
  createTestCase .t undo
  createTestCase .t undo
  createTestCase .t undo
  createTestCase .t undo
  createTestCase .t undo
  createTestCase .t undo
  createTestCase .t undo
  createTestCase .t undo
  createTestCase .t undo
}

alias testState {
  createTestCase .t +slow OG
  createTestCase .t +slow|sv oG
  createTestCase .t -slow og
  createTestCase .t -slow o
  createTestCase .t -slow|sv o
  createTestCase .t undo
  createTestCase .t undo
  createTestCase .t undo
  createTestCase .t undo
  createTestCase .t undo
}

alias testHp {
  createTestCase .t +5 han
  createTestCase .t -5 han
  createTestCase .t thp5 han
  createTestCase .t thp7 han
  createTestCase .t -4 han
  createTestCase .t -12 han
  createTestCase .t +20 han
}

alias runtestcases {
  setupTestCases
  ./timerDuTests -om %du.testcase.index 2300 runtestcase
  set %du.testcase.index 1
}

alias runtestcase {
  if (%du.testcase.index) {
    if ($($+(%,du.testcase.,%du.testcase.index))) {
      var %text = $($+(%,du.testcase.,%du.testcase.index),2)
      /msg $chan %text
      inc %du.testcase.index
    }
  }
}

alias stoptest {
  /timerDuTests off
}
