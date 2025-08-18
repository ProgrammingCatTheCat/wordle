<script setup>
    import axios from 'axios'
    import blankImage from "../assets/images/blank.png"

    const current_cnt = ref(0)
    const current_word = ref('')
    const word_cnt = ref(0)
    const image_matrix = ref(Array(6).fill().map(() => Array(5).fill(blankImage)))

    const loadLetterImages = async (path) => {
        const letterImages = {}
      const letters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'.split('')
        for (const letter of letters) {
            try {
            const module = await import(`${path}/${letter}.png`)
            letterImages[letter] = module.default
            } catch (error) {
            console.warn(`Failed to load image for letter ${letter}`)
            }
        }
        
        return letterImages
    }

    const keyboardRows = [
        ['A', 'B', 'C', 'D', 'E', 'F', 'G'],
        ['H', 'I', 'J', 'K', 'L', 'M', 'N'],
        ['O', 'P', 'Q', 'R', 'S', 'T', 'U'],
        ['V', 'W', 'X', 'Y', 'Z']
    ]

    import { ref, onMounted, nextTick, watch } from 'vue'
    const letterImages = ref({})
    const letterChosenImages = ref({})
    const letterYellowImages = ref({})
    const letterGreenImages = ref({})
    const letterGrayImages = ref({})

    onMounted(async () => {
      letterImages.value = await loadLetterImages(`../assets/images/alphabet`)
      letterGrayImages.value = await loadLetterImages(`../assets/images/alphabet`)
      letterChosenImages.value = await loadLetterImages(`../assets/images/chosen`)
      letterYellowImages.value = await loadLetterImages(`../assets/images/hit`)
      letterGreenImages.value = await loadLetterImages(`../assets/images/present`)
    })

    watch(word_cnt, (newVal, oldVal) => {
      if (newVal === 6) {
        setTimeout(() => {
          alert("You lose...But no worry, can try again!")
          clearMatrix()
        }, 500)
      }
    })

    function clearMatrix() {
      current_cnt.value = 0
      current_word.value = ''
      word_cnt.value = 0
      image_matrix.value = (Array(6).fill().map(() => Array(5).fill(blankImage)))
    }

    const onKeyPress = async (letter) => {
      console.log(`Pressed letter: ${letter}`)
      console.log(current_cnt.value)
      if (current_cnt.value < 5) {
        current_word.value += letter
        console.log(current_word.value)
        image_matrix.value[word_cnt.value][current_cnt.value] = letterChosenImages.value[letter]
        console.log(letterImages.value[letter])
        current_cnt.value += 1
      }
      else{
        alert("Already five letters, please press submit to check word!")
      }
    }

    const onSubmit = async () => {
      if (current_cnt.value === 5) {
        console.log("Submitted word:", current_word.value)

        try {
          const response = await axios.post('http://localhost:8080/api/submitWord', {
            word: current_word.value
          })
          console.log('status:', response.status)
          console.log('response:', response.data)

          if (response.status === 200) {
            const result = response.data.result;
            console.log('result:', result)
            if(result && result[0] === -1){
              alert("Word not in wordlist")
              while(current_cnt.value > 0){
                current_cnt.value -= 1
                image_matrix.value[word_cnt.value][current_cnt.value] = blankImage
              }
              current_word.value = ""
              word_cnt.value -= 1
              console.log("Clear All:", current_word.value)
            }
            else if(result && result.length >= 6) {
              for (let i = 0; i < 5; i++) {
                const letter = current_word.value[i];
                const resultValue = result[i + 1];

                console.log(`Processing letter ${letter} at position ${i} with result ${resultValue}`)
                if (resultValue === 1) {
                  image_matrix.value[word_cnt.value][i] = letterYellowImages.value[letter]
                  console.log("path:", letterYellowImages[letter])
                }
                else if (resultValue === 0) {
                  image_matrix.value[word_cnt.value][i] = letterGrayImages.value[letter]
                  console.log("path:", letterGrayImages[letter])
                }
                else if(resultValue === 2){ // === 2
                  image_matrix.value[word_cnt.value][i] = letterGreenImages.value[letter]
                  console.log("path:", letterGreenImages[letter])
                }
              }
            }
          }

        } catch (error) {
          console.log('error in onSubmit:', error)
        }
        current_cnt.value = 0
        current_word.value =  ""
        word_cnt.value += 1
      }
      else{
        alert("Please input five letters!")
      }
    }

    const onBackspace = async () => {
      if (current_cnt.value > 0) {
        current_word.value = current_word.value.slice(0, -1)
        current_cnt.value -= 1
        image_matrix.value[word_cnt.value][current_cnt.value] = blankImage
        console.log("Backspace:", current_word.value)
      }
    }

    const onClearAll = async () => {
      while(current_cnt.value > 0){
        current_cnt.value -= 1
        image_matrix.value[word_cnt.value][current_cnt.value] = blankImage
      }
      current_word.value = ""
      console.log("Clear All:", current_word.value)
    }


</script>

<template>
    <div class = "wordle-container">
        <div 
            v-for="(row, rowIndex) in image_matrix" 
            :key="rowIndex" 
            :id="`wordrow_${rowIndex + 1}`"
            class="input_matrix"
        >
            <img 
                v-for="(src, colIndex) in row" 
                :key="colIndex" 
                :src="src"  
                :id="`word_${rowIndex + 1}_${colIndex + 1}`"  
                :alt="`Blank Image Row ${rowIndex + 1} Col ${colIndex + 1}`"
                class="empty-word-image"
            />
        </div>

        <div class="keyboard-container">
      <div 
        v-for="(keyboardRow, rowIndex) in keyboardRows" 
        :key="rowIndex" 
        class="keyboard-row"
      >
        <img 
          v-for="letter in keyboardRow" 
          :key="letter" 
          :src="letterImages[letter]" 
          :alt="`Letter ${letter}`"
          :id="`key-${letter}`"
          class="keyboard-key"
          @click="onKeyPress(letter)"
        />
      </div>
        </div>

      <div class="button-container">
        <button id="submit" class="button-style" @click="onSubmit">submit</button>
        <button id="submit" class="button-style" @click="onBackspace">backspace</button>
        <button id="submit" class="button-style" @click="onClearAll">clear all</button>
      </div>
    </div>
</template>

<style scoped>
.wordle-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  width: 100vw; /* 使用视口宽度 */
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

.input_matrix {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 500px;
  margin: 0 auto;
}

.empty-word-image {
  width: 50px;
  height: 50px;
  padding: 5px;
  margin: 0; /* 移除margin，使用padding控制间距 */
}

.keyboard-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 30px;
  gap: 10px;
}

.keyboard-row {
  display: flex;
  justify-content: center;
  gap: 5px;
}

.keyboard-key {
  width: 50px;
  height: 50px;
  padding: 3px;
  cursor: pointer;
  transition: transform 0.1s ease;
}

.keyboard-key:hover {
  transform: scale(1.1);
}

.button-container {
  margin-top: 10px;
}
</style>