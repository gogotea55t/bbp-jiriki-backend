# bbp-jiriki-backend

[![Build Status](https://travis-ci.com/gogotea55t/bbp-jiriki-backend.svg?branch=master)](https://travis-ci.com/gogotea55t/bbp-jiriki-backend)

[![Coverage Status](https://coveralls.io/repos/github/gogotea55t/bbp-jiriki-backend/badge.svg?branch=master)](https://coveralls.io/github/gogotea55t/bbp-jiriki-backend?branch=master)

## Data Modeling

```
@startuml
entity "Users" as us {
   +  userId:varchar(10)
   --
   *  userName:varchar(15)
}

entity "Songs" as so {
   + songId:varchar(10)
   --
   *  jirikiRank:int
   *  songName:varchar(60)
   *  contributor:varchar(20)
   *  instrument:varchar(15)
}

entity "Scores" as sc {
   +  scoreId:int
   --
   #  songs_song_id [FK(songs,song_id)]
   #  users_user_id [FK(users, user_id)]
   *  score:tinyint
}

entity "TwitterUsers" as tw {
   +  twitter_user_id:varchar(30)
   --
   #  users_user_id [FK(users, user_id)]
}

entity "WeeklyChallanges" as wc {
  + weekly_challange_id:int
  --
  # songs_song_id [FK(songs, song_id)]
  * start_date: DateTime
  * end_date: DateTime
}

us ||-do-o{ sc
so ||-up-o{ sc
us ||-ri-o{ tw
so ||-ri-o{wc
@enduml
```