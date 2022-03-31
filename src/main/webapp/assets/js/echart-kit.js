/**
 * 引用 echart-kit.js 中各种 initXxx 方法之后, 返回所需的生成图的参数
 */

/**
 * 将传入的对应的两个数组, 转化成一一对应的object对象数组
 * @param items : string[]
 * @param numbers : number[]
 * @returns {array} : 包含着各数据键值对的数组
 */
function arrayTransObj(items, numbers) {
    let array = new Array(items.length);
    for (let i = 0; i < array.length; i++) {
        array[i] = {name: items[i] , value: numbers[i]}
    }
    return array;
}

/**
 * 通过传入的x, y轴数据, 返回一个包含着直方图参数的对象
 * @param title : string
 * @param x_axis : string[]
 * @param y_axis : number[]
 * @returns object : 一个包含着直方图参数的对象
 */
function initHist(title, x_axis, y_axis) {
    return {
        title: {
            text: title,
            left: 'center'
        },
        toolbox: {
            feature: {
                saveAsImage: {}
            }
        },
        tooltip: {
            trigger: 'axis'
        },
        xAxis: {
            type: 'category',
            data: x_axis
        },
        yAxis: {
            type: 'value'
        },
        series: [
            {
                data: y_axis,
                type: 'bar'
            }
        ]
    };
}

/**
 * 通过传入的各个区间的人数数据, 返回一个包含着饼图参数的对象
 * @param title : string
 * @param items : string[]
 * @param numbers : number[]
 * @returns object : 一个包含着饼图参数的对象
 */
function initPie(title, items, numbers) {
    return {
        title: {
            text: title,
            left: 'center'
        },
        toolbox: {
            feature: {
                saveAsImage: {}
            }
        },
        tooltip: {
            trigger: 'item'
        },
        legend: {
            orient: 'vertical',
            left: 'left',
        },
        series: [{
            name: '分数段',
            type: 'pie',
            radius: '50%',
            data: arrayTransObj(items, numbers),
            emphasis: {
                itemStyle: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
            }
        }]
    };
}

/**
 * 通过传入的x, y轴数据, 返回一个包含着曲线图参数的对象
 * @param title : string
 * @param x_axis : string[]
 * @param y_axis : number[]
 * @param smoothed : boolean
 * @returns object : 一个包含着曲线图参数的对象
 */
function initLine(title, x_axis, y_axis, smoothed) {
    return {
        title: {
            text: title,
            left: 'center'
        },
        toolbox: {
            feature: {
                saveAsImage: {}
            }
        },
        tooltip: {
            trigger: 'axis'
        },
        xAxis: {
            type: 'category',
            data: x_axis
        },
        yAxis: {
            type: 'value'
        },
        series: [{
            data: y_axis,
            type: 'line',
            smooth: smoothed
        }]
    };
}