package main

import (
	"sync"
	"fmt"
	"time"
	"math/rand"
)

func print(Graph *[][]int) {
	size := len(*Graph)
	for i := 0; i < size; i++ {
		for j := 0; j < size; j++ {
			fmt.Print((*Graph)[i][j], " ")
		}
		fmt.Println()
	}
}

func changePrice(first, second int, Graph *[][]int) {
	if first != second {
		var newPrice = rand.Intn(100) + 1;
		(*Graph)[first][second] = newPrice
		(*Graph)[second][first] = newPrice
		fmt.Println("new Price.way form", first, " to ", second, " = ", newPrice);
		print(Graph)

	}
}

func deleteWay(first, second int, Graph *[][]int) {

	(*Graph)[first][second] = 0
	(*Graph)[second][first] = 0
	fmt.Println("delete. way form", first, " to ", second, " = ", 0);
	print(Graph)

}

func ChangePriceThread(mutex *sync.RWMutex, Graph *[][]int) {
	for {

		mutex.Lock()
		numPlaces := len(*Graph)
		if (numPlaces > 1) {
			var first = rand.Intn(numPlaces - 1)
			var second = rand.Intn(numPlaces - 1)
			if (*Graph)[first][second] == 0 {
				changePrice(first, second, Graph)
			}
		}
		mutex.Unlock()
		time.Sleep(370 * time.Millisecond)
		//fmt.Println("change price")
	}
}

func AddDeleteWayThread(mutex *sync.RWMutex, Graph *[][]int) {
	for {

		mutex.Lock()
		numPlaces := len(*Graph)
		if (numPlaces > 1) {
			var first = rand.Intn(numPlaces - 1)
			var second = rand.Intn(numPlaces - 1)
			if (*Graph)[first][second] == 0 {
				changePrice(first, second, Graph)
			} else {
				deleteWay(first, second, Graph)
			}
		}
		mutex.Unlock()
		time.Sleep(300 * time.Millisecond)
		//	fmt.Println("add/delete Way")

	}
}

func AddDeleteTown(mutex *sync.RWMutex, Graph *[][]int) {
	for {
		mutex.Lock()
		numPlaces := len(*Graph)
		var wantToDel bool
		if rand.Intn(2)%2 == 0 {
			wantToDel = true
		} else {
			wantToDel = false
		}
		if numPlaces < 3 {
			wantToDel = false
		}
		if (wantToDel) {
			numPlaces--
			for i := 0; i < numPlaces; i++ {
				(*Graph)[i] = (*Graph)[i][:len((*Graph)[i])-1]
			}
			*Graph = (*Graph)[:len(*Graph)-1]
			fmt.Println("delete place, num = ", numPlaces)

		} else {
			numPlaces++
			var newLine []int
			for i := 0; i < numPlaces-1; i++ {
				(*Graph)[i] = append((*Graph)[i], 0)
				newLine = append(newLine, 0)
			}
			newLine = append(newLine, 0)
			*Graph = append(*Graph, newLine)
			fmt.Println("add new , num = ", numPlaces)

		}

		mutex.Unlock()
		time.Sleep(200 * time.Millisecond)

	}
}

func FindWay(mutex *sync.RWMutex, Graph *[][]int) {

	for {
		mutex.RLock()
		numPlaces := len(*Graph)
		if (numPlaces > 1) {
			var first = rand.Intn(numPlaces - 1)
			var second = rand.Intn(numPlaces - 1)
			if (first != second) {
				if (*Graph)[first][second] != 0 {
					fmt.Println("find Way ", first, " to ", second, " = ", (*Graph)[first][second])

				} else {
					fmt.Println("no Way ", first, " to ", second);
				}

			}

		}
		mutex.RUnlock()

		time.Sleep(150 * time.Millisecond)

	}

}

func main() {
	var mutex sync.RWMutex
	var Graph [][]int

	go AddDeleteTown(&mutex, &Graph)

	go ChangePriceThread(&mutex, &Graph)

	go AddDeleteWayThread(&mutex, &Graph)

	go FindWay(&mutex, &Graph)

	var input string
	fmt.Scanln(&input)
}
