cat pids.txt | while read line
do
    echo "killing process $line"
    kill -9 $line
done