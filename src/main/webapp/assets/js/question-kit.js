function combineContent(max_choices, option_prefix, answer_prefix) {
    var final_options = "";
    var final_answers = "";

    for (let i = 1; i < max_choices; i++) {
        var strForOptions = option_prefix + i;
        var strForAnswers = answer_prefix + i;
        var recentCheckBox = document.getElementById(strForAnswers); // 这个已经是正确的
        var recentText = document.getElementById(strForOptions);
        final_options = final_options + '~~~' + recentText.value;
        final_answers = final_answers + '~~~' + recentCheckBox.checked;
    }

    return {
        full_option: final_options,
        full_answer: final_answers
    }
}
