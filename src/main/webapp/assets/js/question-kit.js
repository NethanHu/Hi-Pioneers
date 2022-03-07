/**
 * 把选择题的选项和答案传入，生成传入数据库的字符串。
 *
 * @param max_choices : number
 * @param option_prefix : string
 * @param answer_prefix : string
 * @returns {{full_answer: string, full_option: string}}
 *
 */
function combineChoiceContent(max_choices, option_prefix, answer_prefix) {
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

/**
 * 把填空题的答案传入，生成传入数据库的字符串。
 *
 * @param max_choices : number
 * @param option_prefix : string
 * @returns {{full_answer: string}}
 *
 */
function combineBlankContent(max_choices, option_prefix) {
    var final_options = "";

    for (let i = 1; i < max_choices; i++) {
        var strForOptions = option_prefix + i;
        var recentText = document.getElementById(strForOptions);
        final_options = final_options + '~~~' + recentText.value;
    }

    return {
        full_answer: final_options
    }
}

