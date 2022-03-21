/**
 * <select> 元素中所需要填充的元素
 */
var question_course = ['', '语文', '数学', '英语', '物理'];
var question_type = ['', '一般选择题', '简答题', '填空题', '证明题'];
var question_level = ['', '1', '2', '3', '4', '5'];

/**
 * 给定需要显示出的题目的各个类别，依次填充 <select> 标签内的 <option> 元素
 *
 * @param question_character : string[]
 * @param target_select_element : HTMLSelectElement
 *
 */
function fillSelectOptions(question_character, target_select_element) {
    for (let i = 0; i < question_character.length; i++) {
        let option_node = document.createElement('option');
        option_node.innerText = question_character[i];
        option_node.setAttribute('value', question_character[i]);
        target_select_element.appendChild(option_node);
    }
}

/**
 * 控制 fill 注入的 choose_question 和 create_paper 两个 html，将备选框题目数据同步
 */

const chosen_questions = [];
var question_index = 0;

/**
 * 构造器，用于创建一个需要添加进备选框的题目的对象，用于加入到 chosen_questions 数组中，以便于在
 * 新的 fill 的备选框 html 中查看到已选择的题目
 *
 * @param question_id : string
 * @param question_content : string
 * @constructor : 生成所选题目对象
 */
function AddedQuestion(question_id, question_content) {
    this.id = question_id;
    this.content = question_content;
}

/**
 * 按钮点击事件：
 * 在 chosen_question 数组中添加了题目对象后，将已选择的题目操作按钮禁用
 */
function banChooseButton() {
    for (let i = 0; i < chosen_questions.length; i++) {
        let target_btn_id = 'btn_' + chosen_questions[i].id;
        let target_btn = document.getElementById(target_btn_id);
        if (target_btn == null) {
            continue;
        }
        target_btn.setAttribute("disabled", "disabled");
    }
}

/**
 * 按钮点击事件：
 * 删除备选框中所选的题目
 *
 * @param question_id : string
 */
function deleteChosenQuestion(question_id) {
    let index = objIndexOf(question_id);
    chosen_questions.splice(index, 1);
    var btn_id = document.getElementById("btn_" + question_id);
    if(btn_id != null) {
        btn_id.removeAttribute("disabled");
    }
    question_index--;
    kit.fill('/admin/paper/createPaper', null, '#create_paper');
}

/**
 * 通过所选题目的 id，来返回在 chosen_question 中的序号
 *
 * @param question_id : string
 * @returns {number} : 在 chosen_number 的 index
 */
function objIndexOf(question_id) {
    for (let i = 0; i < chosen_questions.length; i++) {
        if (chosen_questions[i].id == question_id) {
            return i;
        }
    }
}

/**
 * 传入 Paper.content 中的字符串，返回试卷中所出现题目 id 的数组
 *
 * @param DBString : string
 * @returns {{index: number, indexed_array: *}}
 */
function transContentToArray(DBString) {
    let trans_array = DBString.split("~~~");
    let array_length = trans_array.length;

    return {
        indexed_array : trans_array,
        index : array_length - 1
    }
}
