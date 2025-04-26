for id in {8185..9298} # ids to check between
do
  EXAMINE="$(curl https://api.runelite.net/runelite-1.5.42-SNAPSHOT/examine/npc/$id)"
	echo "{\"id\": $id, \"examine\": \"$EXAMINE\"}," >> npc_examines.json
done
