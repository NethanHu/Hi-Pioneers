const question_course = ['', '语文', '数学', '英语', '物理'];
const question_type = ['', '一般选择题', '简答题', '填空题', '证明题'];
const question_level = ['', '1', '2', '3', '4', '5'];

/**
 * 给定需要显示出的题目的各个类别，依次填充 <select> 标签内的 <option> 元素。
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


var chosen_questions = [];

/**
 * 构造器，用于创建一个需要添加进备选框的题目的对象，用于加入到 chosen_questions 数组中，以便于在新 fill 的备选框 html
 * 中查看到已选择的题目
 *
 * @param question_id : string
 * @param question_content : string
 * @constructor
 */
function AddedQuestion(question_id, question_content) {
    this.id = question_id;
    this.content = question_content;
}