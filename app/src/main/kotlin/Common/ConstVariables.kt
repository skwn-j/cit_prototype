package Common

class ConstVariables {

    companion object {
        /*
        * SharedPreferences variables
        * */
        val USER_IS_LOGINED = false

        val PREF_KEY_AGENT_TYPE = "preferences_agent_type"
        val PREF_AGENT_TYPE_NONE = 0
        val PREF_AGENT_TYPE_FRIEND = 1
        val PREF_AGENT_TYPE_GRAND_CHILD = 2

        /*
        * Evnetbus type variables
        * */
        val EVENTBUS_TYPE_TUTORIAL_DONE = 100
        val EVENTBUS_TRAINING_START = 101
        val EVENTBUS_INPUT_VOICE_START = 102
        val EVENTBUS_INPUT_VOICE_DONE = 103

        /*
        * User Mode
        * */
        val USER_SELECT_TRAINING = 200
        val USER_SELECT_TEST = 201
    }
}