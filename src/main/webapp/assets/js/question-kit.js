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

/**
 * 通过传入的来自数据库的 option 字符串，将其转变成有序的数组传出。
 *
 * @param DBString : string
 * @param type : 可传入"ABCD"或者"1234"
 * @returns {{index: number, indexed_array: Array}}
 *
 */
function transDBSingleString(DBString, type) {
    var trans_array = DBString.split("~~~");
    var array_length = trans_array.length;

    for (let i = 1; i < array_length; i++) {
        switch (type) {
            case 'ABCD' :
                trans_array[i] = String.fromCharCode(64 + i) + '. ' + trans_array[i];
                break;
            case '1234' :
                trans_array[i] = i +  '. ' + trans_array[i];
                break;
        }
    }
    return {
        indexed_array : trans_array,
        index : array_length - 1
    }
}

/**
 * 将数据库的 answer 字符串传入，返回选择题答案字符串。
 *
 * @param DBString : string
 * @returns string
 *
 */
function transDBSingleAnswer(DBString) {
    var trans_array = DBString.split("~~~");
    var array_length = trans_array.length;
    var final_answer = "";

    for (let i = 1; i < array_length; i++) {
        if (trans_array[i] == 'true') {
            final_answer += String.fromCharCode(64 + i);
        }
    }
    return final_answer;
}

