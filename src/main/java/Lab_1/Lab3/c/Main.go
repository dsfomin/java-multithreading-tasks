package main

import (
	"fmt"
	"math/rand"
	"sync"
	"time"
)

func getString(element int) string {
	switch element {
	case 0:
		return "tabacco"
	case 1:
		return "paper"
	case 2:
		return "matches"
	default:
		return "unknown"
	}
}

func smoker(element int, table *[]bool, readSemaphore, writeSemaphore chan bool, waitGroup *sync.WaitGroup) {
	defer waitGroup.Done()
	for {
		readSemaphore <- true
		fmt.Println("Check smoker with", getString(element))
		if !(*table)[element] {
			fmt.Println("Smoker with", getString(element),"is smoking", "\n")
			for i := range *table {
				(*table)[i] = false
			}
			writeSemaphore <- true
		} else {
			<-readSemaphore
			time.Sleep(time.Second)
		}
	}
}

func middleman(table *[]bool, readSemaphore, writeSemaphore chan bool, waitGroup *sync.WaitGroup) {
	defer waitGroup.Done()
	for {
		<-writeSemaphore
		var first, second = getCigaretteStuff()
		fmt.Println("Middleman give:", getString(first), "and", getString(second))
		(*table)[first] = true
		(*table)[second] = true
		<-readSemaphore
	}
}

func getCigaretteStuff() (int, int) {
	r := rand.New(rand.NewSource(time.Now().UnixNano()))

	stuff1 := r.Intn(3)
	stuff2 := r.Intn(3)
	for stuff2 == stuff1 {
		stuff2 = r.Intn(3)
	}

	return stuff1, stuff2
}

func main() {
	const smokersNum = 3
	var waitGroup sync.WaitGroup
	var table = make([]bool, smokersNum)
	var readSemaphore = make(chan bool)
	var writeSemaphore = make(chan bool, 1)
	writeSemaphore <- true

	waitGroup.Add(1)
	go middleman(&table, readSemaphore, writeSemaphore, &waitGroup)
	for i := 0; i < smokersNum; i++ {
		waitGroup.Add(1)
		go smoker(i, &table, readSemaphore, writeSemaphore, &waitGroup)
	}
	waitGroup.Wait()
}