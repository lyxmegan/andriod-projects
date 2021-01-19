package edu.cs371m.triviagame.api

class Repository(private val api: TriviaApi) {
    // XXX Write me.
     suspend fun getThree(): List<TriviaQuestion> {
        return api.getThree("").results
    }
}